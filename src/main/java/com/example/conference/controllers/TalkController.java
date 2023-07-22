package com.example.conference.controllers;

import com.example.conference.models.dtos.*;
import com.example.conference.models.viewmodels.ConferenceVm;
import com.example.conference.models.viewmodels.TalkVm;
import com.example.conference.services.IConferenceService;
import com.example.conference.services.ITalkService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/talks")
public class TalkController {
    public final ITalkService talkService;

    public TalkController(ITalkService talkService) {
        this.talkService = talkService;
    }


    @GetMapping("/{id}")
    public TalkVm getById(@PathVariable Long id) {
        return talkService.getById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TalkVm> update(@PathVariable Long id, @Valid @RequestBody UpdateTalkDto updateTalkDto) {
        var updated = talkService.update(id, updateTalkDto);
        return ResponseEntity
                .ok()
                .body(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        talkService.delete(id);
        return ResponseEntity
                .ok()
                .body("deleted");
    }

}


