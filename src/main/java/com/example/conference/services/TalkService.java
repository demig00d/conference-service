package com.example.conference.services;

import com.example.conference.exceptions.ResourceNotFoundException;
import com.example.conference.models.dtos.CreateTalkDto;
import com.example.conference.models.dtos.UpdateTalkDto;
import com.example.conference.models.viewmodels.TalkVm;
import com.example.conference.repositories.TalkRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TalkService implements ITalkService{
    private final TalkRepository talkRepository;

    public TalkService(TalkRepository talkRepository) {
        this.talkRepository = talkRepository;
    }

    @Override
    public TalkVm getById(Long id) {
        return talkRepository.findById(id)
                .map(TalkVm::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Talk with id " + id + " not found."));
    }

    @Override
    public List<TalkVm> getAllByConferenceId(Long conferenceId) {
        return talkRepository.findAll().stream()
                .map(TalkVm::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public TalkVm create(CreateTalkDto createTalkDto) {
        var entity = createTalkDto.toEntity();
        var saved = talkRepository.save(entity);
        return TalkVm.fromEntity(saved);
    }

    @Override
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
