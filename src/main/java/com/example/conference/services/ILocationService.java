package com.example.conference.services;

import com.example.conference.models.dtos.CreateLocationDto;
import com.example.conference.models.dtos.UpdateLocationDto;
import com.example.conference.models.viewmodels.LocationVm;

import java.util.List;

public interface ILocationService {

    LocationVm getById(Long id);

    List<LocationVm> getAll();

    List<LocationVm> getAllByConferenceId(Long conferenceId);

    LocationVm create(CreateLocationDto createLocationDto);

    LocationVm update(Long id, UpdateLocationDto updateLocationDto);

    void delete(Long id);
}
