package com.example.conference.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.conference.exceptions.ResourceNotFoundException;
import com.example.conference.models.dtos.CreateLocationDto;
import com.example.conference.models.dtos.UpdateLocationDto;
import com.example.conference.models.viewmodels.LocationVm;
import com.example.conference.services.ConferenceService;
import com.example.conference.services.LocationService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocationController.class)
public class LocationControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ConferenceService conferenceService;
    @MockBean
    private LocationService locationService;

    @Test
    public void getReturnsListOfCategories() throws Exception {
        var mockData = List.of(
                new LocationVm(1L, "Moscow", new Date(), new Date()),
                new LocationVm(2L, "Kazan", new Date(), new Date())
        );
        when(locationService.getAll()).thenReturn(mockData);
        var result = mockMvc.perform(get("/api/v1/location"))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        var mapped = mapFromJson(body, LocationVm[].class);
        assertEquals(mapped[0], mockData.get(0));
        assertEquals(mapped[1], mockData.get(1));
    }

    @Test
    public void getWithIdReturnsVm() throws Exception {
        var mockVm = new LocationVm(1L, "Moscow", new Date(), new Date());
        when(locationService.getById(1L)).thenReturn(mockVm);
        var result = mockMvc.perform(get("/api/v1/locations/1"))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        var mapped = mapFromJson(body, LocationVm.class);
        assertEquals(mapped, mockVm);
    }

    @Test
    public void get404OnInvalidId() throws Exception {
        when(locationService.getById(any(Long.class))).thenThrow(new ResourceNotFoundException("Not found"));
        var result = mockMvc.perform(get("/api/v1/locations/1"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void update404OnInvalidId() throws Exception {
        Mockito.doThrow(ResourceNotFoundException.class)
                .when(locationService)
                .update(any(Long.class), any(UpdateLocationDto.class));
        mockMvc.perform(
                        put("/api/v1/locations/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapToJson(new UpdateLocationDto("location")))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void putCallsService() throws Exception {
        mockMvc.perform(
                        put("/api/v1/locations/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapToJson(new UpdateLocationDto("asd"))))
                .andExpect(status().isNoContent());
        verify(locationService).update(any(Long.class), any(UpdateLocationDto.class));
    }

    @Test
    public void deleteCallsService() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/locations/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(locationService).delete(any(Long.class));

    }

    @Test
    public void postCallsCreate() throws Exception {
        var locationDtoArgumentCaptor = ArgumentCaptor.forClass(CreateLocationDto.class);
        var mockVm = new LocationVm();
        mockVm.setId(1L);
        when(locationService.create(any())).thenReturn(mockVm);
        var dto = new CreateLocationDto("asd");
        mockMvc.perform(
                        post("/api/v1/location")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapToJson(dto)))
                .andExpect(status().isCreated());
        verify(locationService).create(locationDtoArgumentCaptor.capture());
        assertEquals(dto, locationDtoArgumentCaptor.getValue());
    }


    @Test
    public void addConferenceCallsService() throws Exception {
        mockMvc.perform(
                        put("/api/v1/locations/1/conference/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(conferenceService).addLocation(1L, 1L);

    }

    @Test
    public void removeConferenceCallsService() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/locations/1/conference/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(conferenceService).removeLocation(1L, 1L);

    }

    @Test
    public void getAllCategoriesByConferenceId() throws Exception {
        var mockData = List.of(
                new LocationVm(1L, "Moscow", new Date(), new Date()),
                new LocationVm(2L, "Kazan", new Date(), new Date())
        );
        when(locationService.getAllByConferenceId(anyLong())).thenReturn(mockData);
        var result = mockMvc.perform(get("/api/v1/conference/1/location"))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        var mapped = mapFromJson(body, LocationVm[].class);
        assertEquals(mapped[0], mockData.get(0));
        assertEquals(mapped[1], mockData.get(1));
    }


    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}
