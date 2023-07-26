package com.example.conference.controllers;

import com.example.conference.models.dtos.CreateConferenceDto;
import com.example.conference.models.dtos.CreateTalkDto;
import com.example.conference.models.dtos.UpdateConferenceDto;
import com.example.conference.models.dtos.UpdatePartiallyConferenceDto;
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
@RequestMapping(value = "/api/v1")
public class ConferenceController {
    public final IConferenceService conferenceService;
    public final ITalkService talkService;

    public ConferenceController(IConferenceService conferenceService, ITalkService talkService) {
        this.conferenceService = conferenceService;
        this.talkService = talkService;
    }

    @GetMapping("/conferences")
    public List<ConferenceVm> getAll() {
        return conferenceService.getAll();
    }

    @GetMapping("/conferences/{id}")
    public ConferenceVm get(@PathVariable Long id) {
        return conferenceService.getById(id);
    }

    @PostMapping("/conferences")
    public ResponseEntity<ConferenceVm> create(@Valid @RequestBody CreateConferenceDto createConferenceDto, UriComponentsBuilder uriComponentsBuilder) {
        var created = conferenceService.create(createConferenceDto);
        var urlLocation = uriComponentsBuilder.path("/api/v1/conferences/{id}").buildAndExpand(created.getId());
        return ResponseEntity
                .created(urlLocation.toUri())
                .body(created);
    }

    @PutMapping("/conferences/{id}")
    public ResponseEntity<ConferenceVm> update(@PathVariable Long id, @Valid @RequestBody UpdateConferenceDto updateConferenceDto) {
        var updated = conferenceService.update(id, updateConferenceDto);
        return ResponseEntity
                .ok()
                .body(updated);
    }

    @PatchMapping("/conferences/{id}")
    public ResponseEntity<ConferenceVm> updatePartially(@PathVariable Long id, @Valid @RequestBody UpdatePartiallyConferenceDto updatePartiallyConferenceDto) {
        var updated = conferenceService.updatePartially(id, updatePartiallyConferenceDto);
        return ResponseEntity
                .ok()
                .body(updated);
    }


    @DeleteMapping("/conferences/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        conferenceService.delete(id);
        return ResponseEntity
                .ok()
                .body("deleted");
    }

    @GetMapping("/conferences/{id}/talks")
    public List<TalkVm> getAllByConferenceId(@PathVariable Long id) {
        return talkService.getAllByConferenceId(id);
    }

    @PostMapping("/conferences/{id}/talks")
    public ResponseEntity<TalkVm> create(
            @PathVariable Long id,
            @Valid @RequestBody CreateTalkDto createTalkDto,
            UriComponentsBuilder uriComponentsBuilder) {
        var created = talkService.create(id, createTalkDto);
        var urlLocation = uriComponentsBuilder.path("/api/v1/conferences/" + id + "/talks").buildAndExpand(created.getId());
        return ResponseEntity
                .created(urlLocation.toUri())
                .body(created);
    }

    @GetMapping(value = "/locations/{locationId}/conference")
    public List<ConferenceVm> getAllConferencesByLocationId(@PathVariable Long locationId) {
        return conferenceService.getAllByLocationId(locationId);
    }
}


