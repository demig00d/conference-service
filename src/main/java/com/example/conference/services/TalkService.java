package com.example.conference.services;

import com.example.conference.exceptions.ResourceNotFoundException;
import com.example.conference.models.dtos.CreateTalkDto;
import com.example.conference.models.dtos.UpdateTalkDto;
import com.example.conference.models.viewmodels.TalkVm;
import com.example.conference.repositories.ConferenceRepository;
import com.example.conference.repositories.TalkRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TalkService implements ITalkService{
    private final TalkRepository talkRepository;
    private final ConferenceRepository conferenceRepository;

    public TalkService(TalkRepository talkRepository, ConferenceRepository conferenceRepository) {
        this.talkRepository = talkRepository;
        this.conferenceRepository = conferenceRepository;
    }

    @Override
    public TalkVm getById(Long id) {
        return talkRepository.findById(id)
                .map(TalkVm::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Talk with id " + id + " not found."));
    }

    @Override
    public List<TalkVm> getAllByConferenceId(Long conferenceId) {
        return conferenceRepository
                .findByIdPrefetchTalks(conferenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Conference with id " + conferenceId + " not found."))
                .getTalks()
                .stream()
                .map(TalkVm::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public TalkVm create(Long conferenceId, CreateTalkDto createTalkDto) {
        var conference = conferenceRepository
                .findById(conferenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Conference with id " + conferenceId + " does not exist."));
        var newComment = createTalkDto.toEntity(conference);
        var savedComment = talkRepository.save(newComment);
        return TalkVm.fromEntity(savedComment);
    }    @Override

    public TalkVm update(Long id, UpdateTalkDto updateTalkDto) {
        var entity = talkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conference with id " + id + " not found."));

        updateTalkDto.updateEntity(entity);
        var saved = talkRepository.save(entity);
        return TalkVm.fromEntity(saved);
    }

    @Override
    public void delete(Long id) {
        try {
            talkRepository.deleteById(id);
        } catch (Exception ignored) {
            throw new ResourceNotFoundException("Conference with id " + id + " not found.");
        }
    }


}
