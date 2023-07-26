package com.example.conference.controllers;

import com.example.conference.exceptions.ResourceNotFoundException;
import com.example.conference.models.dtos.CreateTalkDto;
import com.example.conference.models.dtos.UpdateTalkDto;
import com.example.conference.models.viewmodels.TalkVm;
import com.example.conference.services.TalkService;
import com.example.conference.utils.FakeCalendar;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TalkController.class)
public class TalkControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TalkService talkService;

    @Test
    public void getReturnsTalk() throws Exception {
        var mockVm = new TalkVm(
                1L,
                "title1",
                "description1",
                1L, FakeCalendar.LOCAL_DATE_TIME, FakeCalendar.LOCAL_DATE_TIME,
                new Date(), new Date()
        );
        when(talkService.getById(1L)).thenReturn(mockVm);
        var result = mockMvc.perform(get("/api/v1/talks/1"))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        var mapped = mapFromJson(body, TalkVm.class);
        assertEquals(mapped, mockVm);
    }

    @Test
    public void getTalksForConferenceReturnsVms() throws Exception {
        var mockVm1 = new TalkVm(
                1L,
                "title1",
                "description1",
                1L, FakeCalendar.LOCAL_DATE_TIME, FakeCalendar.LOCAL_DATE_TIME,
                new Date(), new Date()
        );
        var mockVm2 = new TalkVm(
                2L,
                "title2",
                "description2",
                1L, FakeCalendar.LOCAL_DATE_TIME, FakeCalendar.LOCAL_DATE_TIME,
                new Date(), new Date()
        );
        var mockVm3 = new TalkVm(
                3L,
                "title3",
                "description3",
                1L, FakeCalendar.LOCAL_DATE_TIME, FakeCalendar.LOCAL_DATE_TIME,
                new Date(), new Date()
        );

        var mockData = List.of(mockVm1, mockVm2, mockVm3);
        when(talkService.getAllByConferenceId(anyLong())).thenReturn(mockData);
        var result = mockMvc.perform(get("/api/v1/conference/1/talk"))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        var mapped = mapFromJson(body, TalkVm[].class);
        assertEquals(mapped[0], mockData.get(0));
        assertEquals(mapped[1], mockData.get(1));
    }

    @Test
    public void createCallsService() throws Exception {
        var talkDtoArgumentCaptor = ArgumentCaptor.forClass(CreateTalkDto.class);
        var mockVm = new TalkVm();
        mockVm.setId(1L);
        when(talkService.create(anyLong(), any(CreateTalkDto.class))).thenReturn(mockVm);
        var dto = new CreateTalkDto("title", "description", FakeCalendar.LOCAL_DATE_TIME, FakeCalendar.LOCAL_DATE_TIME);
        mockMvc.perform(
                        post("/api/v1/conference/1/talk")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapToJson(dto)))
                .andExpect(status().isCreated());
        verify(talkService).create(eq(1L), talkDtoArgumentCaptor.capture());
        assertEquals(dto, talkDtoArgumentCaptor.getValue());
    }

    @Test
    public void updateCallsService() throws Exception {
        var mockDto = new UpdateTalkDto("title", "description", null, FakeCalendar.LOCAL_DATE_TIME, FakeCalendar.LOCAL_DATE_TIME);

        mockMvc.perform(
                        put("/api/v1/talks/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapToJson(mockDto)))
                .andExpect(status().isNoContent());
        verify(talkService).update(any(Long.class), any(UpdateTalkDto.class));
    }

    @Test
    public void update404OnInvalidId() throws Exception {
        var mockDto = new UpdateTalkDto("title", "description", null, FakeCalendar.LOCAL_DATE_TIME, FakeCalendar.LOCAL_DATE_TIME);

        Mockito.doThrow(ResourceNotFoundException.class)
                .when(talkService)
                .update(any(Long.class), any(UpdateTalkDto.class));
        mockMvc.perform(
                        put("/api/v1/talks/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapToJson(mockDto))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCallsService() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/talks/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(talkService).delete(any(Long.class));
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}
