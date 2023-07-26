package com.example.conference.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.conference.exceptions.ResourceNotFoundException;
import com.example.conference.models.dtos.CreateConferenceDto;
import com.example.conference.models.dtos.UpdateConferenceDto;
import com.example.conference.models.viewmodels.ConferenceVm;
import com.example.conference.services.ConferenceService;
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
import java.util.*;
import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ConferenceController.class)
public class ConferenceControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ConferenceService conferenceService;

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    @Test
    public void getReturnsEmptyArrayIfDbIsEmpty() throws Exception {
        when(conferenceService.getAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/v1/conference"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    public void getReturnsListOfConferences() throws Exception {
        var mockVm1 = new ConferenceVm(
                1L,
                "ScalaConf 2019",
                "description1",
                new HashSet<>(),
                LocalDate.now(),
                LocalDate.now(),
                new Date(),
                new Date()
        );

        var mockVm2 = new ConferenceVm(
                2L,
                "Zymposiums",
                "description2",
                new HashSet<>(),
                LocalDate.now(),
                LocalDate.now(),
                new Date(),
                new Date()
        );

        var mockData = List.of(mockVm1, mockVm2);
        when(conferenceService.getAll()).thenReturn(mockData);
        var result = mockMvc.perform(get("/api/v1/conferences"))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        var mapped = mapFromJson(body, ConferenceVm[].class);
        assertEquals(mapped[0], mockData.get(0));
        assertEquals(mapped[1], mockData.get(1));
    }

    @Test
    public void getWithIdReturnsVm() throws Exception {
        var mockVm = new ConferenceVm(
                1L,
                "ScalaConf 2019",
                "description1",
                new HashSet<>(),
                LocalDate.now(),
                LocalDate.now(),
                new Date(),
                new Date()
        );
        when(conferenceService.getById(1L)).thenReturn(mockVm);
        var result = mockMvc.perform(get("/api/v1/conferences/1"))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        var mapped = mapFromJson(body, ConferenceVm.class);
        assertEquals(mapped, mockVm);
    }

    @Test
    public void get404OnInvalidId() throws Exception {
        when(conferenceService.getById(any(Long.class))).thenThrow(new ResourceNotFoundException("Not found"));
        var result = mockMvc.perform(get("/api/v1/conferences/1"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void update404OnInvalidId() throws Exception {
        var mockDto = new UpdateConferenceDto(
                "asd",
                "asd",
                new HashSet<>(),
                LocalDate.now(),
                LocalDate.now()
        );
        Mockito.doThrow(ResourceNotFoundException.class)
                .when(conferenceService)
                .update(any(Long.class), any(UpdateConferenceDto.class));
        mockMvc.perform(
                        put("/api/v1/conferences/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapToJson(mockDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void putCallsService() throws Exception {
        var mockDto = new UpdateConferenceDto(
                "asd",
                "asd",
                new HashSet<>(),
                LocalDate.now(),
                LocalDate.now()
        );

        mockMvc.perform(
                        put("/api/v1/conferences/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapToJson(mockDto)))
                .andExpect(status().isNoContent());
        verify(conferenceService).update(any(Long.class), any(UpdateConferenceDto.class));
    }

    @Test
    public void deleteCallsService() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/conferences/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(conferenceService).delete(any(Long.class));

    }

    @Test
    public void postCallsCreate() throws Exception {
        var conferenceArgumentCaptor = ArgumentCaptor.forClass(CreateConferenceDto.class);
        var mockVm = new ConferenceVm();
        mockVm.setId(1L);
        when(conferenceService.create(any())).thenReturn(mockVm);

        var dto = new CreateConferenceDto("title", "asd", new HashSet<>(), LocalDate.now(), LocalDate.now());
        mockMvc.perform(
                        post("/api/v1/conference")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapToJson(dto)))
                .andExpect(status().isCreated());
        verify(conferenceService).create(conferenceArgumentCaptor.capture());
        assertEquals(dto, conferenceArgumentCaptor.getValue());
    }


    @Test
    public void postReturnsBadRequestOnInvalidDto() throws Exception {
        var conferenceArgumentCaptor = ArgumentCaptor.forClass(CreateConferenceDto.class);
        var dto = new CreateConferenceDto();
        mockMvc.perform(
                        post("/api/v1/conference")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapToJson(dto)))
                .andExpect(status().isBadRequest());
    }
}
