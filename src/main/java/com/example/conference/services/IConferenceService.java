package com.example.conference.services;

import com.example.conference.models.CreateConferenceDto;
import com.example.conference.models.ConferenceVm;
import com.example.conference.models.UpdateConferenceDto;

import java.util.List;

public interface IConferenceService {
        ConferenceVm getById(Long id);
        List<ConferenceVm> getAll();
        ConferenceVm create(CreateConferenceDto createConferenceDto);
        ConferenceVm update(Long id, UpdateConferenceDto updateConferenceDto);
        void delete(Long id);
}
