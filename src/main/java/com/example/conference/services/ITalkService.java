package com.example.conference.services;

import com.example.conference.models.dtos.CreateTalkDto;
import com.example.conference.models.dtos.UpdateTalkDto;
import com.example.conference.models.viewmodels.TalkVm;

import java.util.List;

public interface ITalkService {
    TalkVm getById(Long id);
    List<TalkVm> getAllByConferenceId(Long conferenceId);
    TalkVm create(CreateTalkDto createTalkeDto);
    TalkVm update(Long id, UpdateTalkDto updateTalkDto);
    void delete(Long id);
}
