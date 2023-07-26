package co;


import com.example.conference.entities.Conference;
import com.example.conference.entities.Location;
import com.example.conference.entities.Talk;
import com.example.conference.exceptions.ResourceNotFoundException;
import com.example.conference.models.dtos.CreateConferenceDto;
import com.example.conference.models.dtos.UpdateConferenceDto;
import com.example.conference.models.viewmodels.ConferenceVm;
import com.example.conference.repositories.ConferenceRepository;
import com.example.conference.services.ConferenceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ConferenceServiceTests {
    @Mock
    ConferenceRepository mockConferenceRepository;
    @InjectMocks
    ConferenceService service;

    @Test
    public void getAllReadsAllConferencesFromDb() {
        when(mockConferenceRepository.findAll()).then(invocation -> new ArrayList<ConferenceVm>());
        var x = service.getAll();
        assertTrue(x.isEmpty());
        verify(mockConferenceRepository).findAll();
    }

    @Test
    public void getByIdReturnsConference() {
        var fakeConference = Optional.of(new Conference()).map(conference -> {
            conference.setId(1L);
            return conference;
        });
        when(mockConferenceRepository.findById(any(Long.class))).thenReturn(fakeConference);
        var res = service.getById(1L);
        verify(mockConferenceRepository).findById(1L);
        assertEquals(res.getId(), 1L);
    }

    @Test
    public void getByIdThrowsOnNotFound() {
        Optional<Conference> missingConference = Optional.empty();
        when(mockConferenceRepository.findById(any(Long.class))).thenReturn(missingConference);
        assertThrows(ResourceNotFoundException.class, () -> service.getById(1L));
    }

    @Test
    public void createAddsConferenceToDb() {
        var fakeConference = new Conference();
        fakeConference.setId(1L);
        when(mockConferenceRepository.save(any(Conference.class))).thenReturn(fakeConference);
        var createDto = new CreateConferenceDto("Title", "Some description", new HashSet<Talk>(), null, null);
        var conferenceArgumentCaptor = ArgumentCaptor.forClass(Conference.class);

        service.create(createDto);
        verify(mockConferenceRepository).save(conferenceArgumentCaptor.capture());

        var captured = conferenceArgumentCaptor.getValue();
        assertEquals(captured.getTitle(), createDto.getTitle());
        assertEquals(captured.getDescription(), createDto.getDescription());
    }

    @Test
    public void updateThrowsOnNotFound() {
        Optional<Conference> missingConference = Optional.empty();
        when(mockConferenceRepository.findById(any(Long.class))).thenReturn(missingConference);
        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, new UpdateConferenceDto()));
    }

    @Test
    public void updateChangesEntity() {
        var fakeConference = new Conference("Title", "Some description", new HashSet<Talk>(), new HashSet<Location>(),null, null);
        fakeConference.setId(1L);
        when(mockConferenceRepository.findById(1L)).thenReturn(Optional.of(fakeConference));
        var conferenceArgumentCaptor = ArgumentCaptor.forClass(Conference.class);
        var dto = new UpdateConferenceDto();
        dto.setTitle("New title");
        dto.setTalks(new HashSet<>());

        service.update(1L, dto);

        verify(mockConferenceRepository).save(conferenceArgumentCaptor.capture());
        var captured = conferenceArgumentCaptor.getValue();

        assertEquals(captured.getTitle(), fakeConference.getTitle());
        assertEquals(captured.getDescription(), dto.getDescription());

    }

    @Test
    public void deleteCallsDb() {
        service.delete(1L);
        verify(mockConferenceRepository).deleteById(1L);
    }

}
