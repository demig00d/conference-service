package com.example.conference.services;

import com.example.conference.entities.Location;
import com.example.conference.exceptions.ResourceNotFoundException;
import com.example.conference.models.dtos.CreateLocationDto;
import com.example.conference.models.dtos.UpdateLocationDto;
import com.example.conference.models.viewmodels.LocationVm;
import com.example.conference.repositories.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService implements ILocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public LocationVm getById(Long id) {
        return locationRepository
                .findById(id)
                .map(LocationVm::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Location with id " + id + " not found"));
    }

    @Override
    public List<LocationVm> getAll() {
        return locationRepository
                .findAll()
                .stream()
                .map(LocationVm::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LocationVm> getAllByConferenceId(Long conferenceId) {
        return locationRepository
                .findAllByConferenceId(conferenceId)
                .stream()
                .map(LocationVm::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public LocationVm create(CreateLocationDto createLocationDto) {
        var location = new Location(createLocationDto.getTitle());
        var savedLocation = locationRepository.save(location);
        return LocationVm.fromEntity(savedLocation);
    }

    @Override
    public LocationVm update(Long id, UpdateLocationDto updateLocationDto) {
        var location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location with id " + id + " not found"));
        if (updateLocationDto.getTitle() != null)
            location.setTitle(updateLocationDto.getTitle());
        var updatedLocation = locationRepository.save(location);
        return LocationVm.fromEntity(updatedLocation);
    }

    @Override
    public void delete(Long id) {
        try {
            locationRepository.deleteById(id);
        } catch (Exception ignored) {
            throw new ResourceNotFoundException("Location with id " + id + " not found.");
        }
    }
}
