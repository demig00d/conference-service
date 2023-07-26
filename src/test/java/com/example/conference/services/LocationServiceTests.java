package com.example.conference.services;

import com.example.conference.entities.Conference;
import com.example.conference.entities.Location;
import com.example.conference.exceptions.ResourceNotFoundException;
import com.example.conference.models.dtos.CreateConferenceDto;
import com.example.conference.models.dtos.UpdateConferenceDto;
import com.example.conference.models.dtos.CreateLocationDto;
import com.example.conference.models.dtos.UpdateLocationDto;
import com.example.conference.models.viewmodels.LocationVm;
import com.example.conference.repositories.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class LocationServiceTests {
    @Mock
    LocationRepository mockLocationRepository;
    @InjectMocks
    LocationService service;

    @Test
    public void getByIdReturnsVm() {
        var fakeLocation = Optional.of(new Location()).map(location -> {
            location.setId(1L);
            return location;
        });
        when(mockLocationRepository.findById(any(Long.class))).thenReturn(fakeLocation);
        var res = service.getById(1L);
        verify(mockLocationRepository).findById(1L);
        assertEquals(res.getId(), 1L);
    }

    @Test
    public void getAllReadsAllCategoriesFromDb() {
        when(mockLocationRepository.findAll()).then(invocation -> new ArrayList<LocationVm>());
        var x = service.getAll();
        assertTrue(x.isEmpty());
        verify(mockLocationRepository).findAll();
    }

    @Test
    public void getByIdThrowsOnNotFound() {
        Optional<Location> none = Optional.empty();
        when(mockLocationRepository.findById(any(Long.class))).thenReturn(none);
        assertThrows(ResourceNotFoundException.class, () -> service.getById(1L));
    }

    @Test
    public void getAllByConferenceIdReturnsLocation() {
        var fakeLocation1 = new Location();
        fakeLocation1.setId(1L);
        var fakeLocation2 = new Location();
        fakeLocation2.setId(2L);
        var fakeCategories = List.of(fakeLocation1, fakeLocation2);
        when(mockLocationRepository.findAllByConferenceId(any(Long.class))).thenReturn(fakeCategories);

        var res = service.getAllByConferenceId(1L);

        verify(mockLocationRepository).findAllByConferenceId(1L);
        var resIds = res.stream()
                .map(LocationVm::getId)
                .mapToLong(x -> x).toArray();
        assertArrayEquals(new long[]{1L, 2L}, resIds);
    }

    @Test
    public void createAddsLocationToDb() {
        var fakeLocation = new Location();
        fakeLocation.setId(1L);
        when(mockLocationRepository.save(any(Location.class))).thenReturn(fakeLocation);
        var createDto = new CreateLocationDto();
        var locationArgumentCaptor = ArgumentCaptor.forClass(Location.class);

        service.create(createDto);
        verify(mockLocationRepository).save(locationArgumentCaptor.capture());

        var captured = locationArgumentCaptor.getValue();
        assertEquals(captured.getTitle(), createDto.getTitle());
    }

    @Test
    public void updateThrowsIfNOtFound() {
        Optional<Location> none = Optional.empty();
        when(mockLocationRepository.findById(any(Long.class))).thenReturn(none);
        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, new UpdateLocationDto()));
    }

    @Test
    public void updateChangesEntity() {
        var fakeLocation = new Location("Name");
        fakeLocation.setId(1L);
        when(mockLocationRepository.findById(1L)).thenReturn(Optional.of(fakeLocation));
        var conferenceArgumentCaptor = ArgumentCaptor.forClass(Location.class);
        var dto = new UpdateLocationDto();
        dto.setTitle("new Title");

        service.update(1L, dto);

        verify(mockLocationRepository).save(conferenceArgumentCaptor.capture());
        var captured = conferenceArgumentCaptor.getValue();

        assertEquals(captured.getTitle(), dto.getTitle());
    }

    @Test
    public void deleteCallsDb() {
        service.delete(1L);
        verify(mockLocationRepository).deleteById(1L);
    }
}
