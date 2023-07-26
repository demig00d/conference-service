package com.example.conference.services;

import com.example.conference.entities.Conference;
import com.example.conference.entities.Location;
import com.example.conference.entities.Talk;
import com.example.conference.exceptions.ResourceNotFoundException;
import com.example.conference.models.dtos.CreateLocationDto;
import com.example.conference.models.dtos.UpdateLocationDto;
import com.example.conference.models.viewmodels.LocationVm;
import com.example.conference.models.dtos.CreateTalkDto;
import com.example.conference.models.dtos.UpdateTalkDto;
import com.example.conference.models.viewmodels.TalkVm;
import com.example.conference.repositories.ConferenceRepository;
import com.example.conference.repositories.TalkRepository;
import com.example.conference.utils.FakeCalendar;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TalkServiceTests {
    @Mock
    TalkRepository mockTalkRepository;
    @Mock
    ConferenceRepository mockConferenceRepository;
    @InjectMocks
    TalkService service;

    @Test
    public void getByIdReturnsVm() {
        var fc = new Talk();
        fc.setConference(new Conference());
        var fakeTalk = Optional.of(fc).map(talk -> {
            talk.setId(1L);
            return talk;
        });
        when(mockTalkRepository.findById(any(Long.class))).thenReturn(fakeTalk);
        var res = service.getById(1L);
        verify(mockTalkRepository).findById(1L);
        assertEquals(res.getId(), 1L);
    }

    @Test
    public void getByIdThrowsOnNotFound() {
        Optional<Talk> none = Optional.empty();
        when(mockTalkRepository.findById(any(Long.class))).thenReturn(none);
        assertThrows(ResourceNotFoundException.class, () -> service.getById(1L));
    }

    @Test
    public void getTalksByConferenceIdReturnsVms() {
        var fakeTalk1 = new Talk();
        fakeTalk1.setId(1L);
        fakeTalk1.setConference(new Conference());
        var fakeTalk2 = new Talk();
        fakeTalk2.setId(2L);
        fakeTalk2.setConference(new Conference());
        var fakeConference = mock(Conference.class);
        var fakeTalks = Set.of(fakeTalk1, fakeTalk2);
        when(fakeConference.getTalks()).thenReturn(fakeTalks);
        when(mockConferenceRepository.findByIdPrefetchTalks(any(Long.class))).thenReturn(Optional.of(fakeConference));

        var res = service.getAllByConferenceId(1L);

        verify(mockConferenceRepository).findByIdPrefetchTalks(1L);
        assertEquals(2, res.size());
    }

    @Test
    public void createTalkAddsTalkToDb() {
        var fakeTalk = new Talk();
        fakeTalk.setId(1L);
        var fakeConference = new Conference();
        fakeConference.setId(1L);
        fakeTalk.setConference(fakeConference);
        when(mockConferenceRepository.findById(anyLong())).thenReturn(Optional.of(fakeConference));
        when(mockTalkRepository.save(any(Talk.class))).thenReturn(fakeTalk);
        var createDto = new CreateTalkDto();
        var locationArgumentCaptor = ArgumentCaptor.forClass(Talk.class);

        service.create(1L, createDto);

        verify(mockTalkRepository).save(locationArgumentCaptor.capture());
        var captured = locationArgumentCaptor.getValue();
        assertEquals(captured.getTitle(), createDto.getTitle());
        assertEquals(captured.getDescription(), createDto.getDescription());
    }

    @Test
    public void updateThrowsIfNOtFound() {
        Optional<Talk> none = Optional.empty();
        when(mockTalkRepository.findById(any(Long.class))).thenReturn(none);
        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, new UpdateTalkDto()));
    }

    @Test
    public void updateChangesEntity() {
        var fakeTalk = new Talk("title", "description", new Conference(), FakeCalendar.LOCAL_DATE_TIME, FakeCalendar.LOCAL_DATE_TIME);
        fakeTalk.setId(1L);
        when(mockTalkRepository.findById(1L)).thenReturn(Optional.of(fakeTalk));
        var conferenceArgumentCaptor = ArgumentCaptor.forClass(Talk.class);
        var dto = new UpdateTalkDto();
        dto.setTitle("new Title");

        service.update(1L, dto);

        verify(mockTalkRepository).save(conferenceArgumentCaptor.capture());
        var captured = conferenceArgumentCaptor.getValue();
        assertEquals(captured.getTitle(), dto.getTitle());
    }

    @Test
    public void deleteCallsDb() {
        service.delete(1L);
        verify(mockTalkRepository).deleteById(1L);
    }

}
