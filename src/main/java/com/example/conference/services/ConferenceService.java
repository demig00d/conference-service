package com.example.conference.services;

import com.example.conference.exceptions.ResourceNotFoundException;
import com.example.conference.models.CreateConferenceDto;
import com.example.conference.models.ConferenceVm;
import com.example.conference.models.UpdateConferenceDto;
import com.example.conference.repositories.ConferenceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConferenceService implements IConferenceService{
    private final ConferenceRepository conferenceRepository;

    public ConferenceService(ConferenceRepository conferenceRepository) {
        this.conferenceRepository = conferenceRepository;
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

        if (updateConferenceDto.getTitle() != null)
            entity.setTitle(updateConferenceDto.getTitle());

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
