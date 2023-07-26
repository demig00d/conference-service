package com.example.conference.services;

import com.example.conference.models.dtos.CreateConferenceDto;
import com.example.conference.models.dtos.UpdateConferenceDto;
import com.example.conference.models.viewmodels.ConferenceVm;

import java.util.List;

public interface IConferenceService {
        ConferenceVm getById(Long id);
        List<ConferenceVm> getAll();
        ConferenceVm create(CreateConferenceDto createConferenceDto);
        ConferenceVm update(Long id, UpdateConferenceDto updateConferenceDto);
        void delete(Long id);
        void removeLocation(Long conferenceId, Long locationId);
        void addLocation(Long conferenceId, Long locationId);
        List<ConferenceVm> getAllByLocationId(Long locationId);
}