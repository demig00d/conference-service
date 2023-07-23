package com.example.conference.services;

import com.example.conference.exceptions.ResourceNotFoundException;
import com.example.conference.models.dtos.CreateConferenceDto;
import com.example.conference.models.dtos.UpdateConferenceDto;
import com.example.conference.models.dtos.UpdatePartiallyConferenceDto;
import com.example.conference.models.viewmodels.ConferenceVm;
import com.example.conference.repositories.ConferenceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConferenceService implements IConferenceService{
    private final ConferenceRepository conferenceRepository;
    private ITalkService talkService;

    public ConferenceService(ConferenceRepository conferenceRepository, ITalkService talkService) {
        this.conferenceRepository = conferenceRepository;
        this.talkService = talkService;
    }

    @Override
    public ConferenceVm getById(Long id) {
        return conferenceRepository.findById(id)
                .map(ConferenceVm::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Conference with id " + id + " not found."));
    }

    @Override
    public List<ConferenceVm> getAll() {
        return conferenceRepository.findAll().stream()
                .map(ConferenceVm::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ConferenceVm create(CreateConferenceDto createConferenceDto) {
        var entity = createConferenceDto.toEntity();
        var saved = conferenceRepository.save(entity);
        return ConferenceVm.fromEntity(saved);
    }

    @Override
    public ConferenceVm update(Long id, UpdateConferenceDto updateConferenceDto) {
        var entity = conferenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conference with id " + id + " not found."));

        updateConferenceDto.updateEntity(entity);
        var saved = conferenceRepository.save(entity);
        return ConferenceVm.fromEntity(saved);
    }

    @Override
    public ConferenceVm updatePartially(Long id, UpdatePartiallyConferenceDto updatePartiallyConferenceDto) {
        var entity = conferenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conference with id " + id + " not found."));

        // ToDo: add mapper
        if (updatePartiallyConferenceDto.getTitle() != null)
            entity.setTitle(updatePartiallyConferenceDto.getTitle());
        if (updatePartiallyConferenceDto.getDescription() != null)
            entity.setDescription(updatePartiallyConferenceDto.getDescription());
        if (updatePartiallyConferenceDto.getStartDate() != null)
            entity.setStartDate(updatePartiallyConferenceDto.getStartDate());
        if (updatePartiallyConferenceDto.getEndDate() != null)
            entity.setEndDate(updatePartiallyConferenceDto.getEndDate());

        var saved = conferenceRepository.save(entity);
        return ConferenceVm.fromEntity(saved);
    }

    @Override
    public void delete(Long id) {
        try {
            conferenceRepository.deleteById(id);
        } catch (Exception ignored) {
            throw new ResourceNotFoundException("Conference with id " + id + " not found.");
        }
    }


}
