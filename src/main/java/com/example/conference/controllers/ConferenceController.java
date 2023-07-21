package com.example.conference.controllers;

import com.example.conference.models.CreateConferenceDto;
import com.example.conference.models.ConferenceVm;
import com.example.conference.models.UpdateConferenceDto;
import com.example.conference.models.UpdatePartiallyConferenceDto;
import com.example.conference.services.IConferenceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/conferences")
public class ConferenceController {
    public final IConferenceService conferenceService;

    public ConferenceController(IConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }
    @GetMapping()
    public List<ConferenceVm> getAll() {
        return conferenceService.getAll();
    }

    @GetMapping("/{id}")
    public ConferenceVm get(@PathVariable Long id) {
        return conferenceService.getById(id);
    }

    @PostMapping()
    public ResponseEntity<ConferenceVm> create(@Valid @RequestBody CreateConferenceDto createConferenceDto, UriComponentsBuilder uriComponentsBuilder) {
        var created = conferenceService.create(createConferenceDto);
        var urlLocation = uriComponentsBuilder.path("/api/v1/conferences/{id}").buildAndExpand(created.getId());
        return ResponseEntity
                .created(urlLocation.toUri())
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConferenceVm> update(@PathVariable Long id, @Valid @RequestBody UpdateConferenceDto updateConferenceDto) {
        var updated = conferenceService.update(id, updateConferenceDto);
        return ResponseEntity
                .ok()
                .body(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ConferenceVm> updatePartially(@PathVariable Long id, @Valid @RequestBody UpdatePartiallyConferenceDto updatePartiallyConferenceDto) {
        var updated = conferenceService.updatePartially(id, updatePartiallyConferenceDto);
        return ResponseEntity
                .ok()
                .body(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        conferenceService.delete(id);
        return ResponseEntity
                .ok()
                .body("deleted");
    }
}


