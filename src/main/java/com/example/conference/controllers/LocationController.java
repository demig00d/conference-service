package com.example.conference.controllers;

import com.example.conference.models.dtos.CreateLocationDto;
import com.example.conference.models.dtos.UpdateLocationDto;
import com.example.conference.models.viewmodels.LocationVm;
import com.example.conference.services.IConferenceService;
import com.example.conference.services.ILocationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class LocationController {

    private final ILocationService locationService;
    private final IConferenceService conferenceService;


    public LocationController(ILocationService locationService, IConferenceService conferenceService) {
        this.locationService = locationService;
        this.conferenceService = conferenceService;
    }

    @GetMapping("/locations")
    public List<LocationVm> getAll() {
        return locationService.getAll();
    }

    @GetMapping("/locations/{id}")
    public LocationVm getById(@PathVariable long id) {
        return locationService.getById(id);
    }

    @GetMapping("/conferences/{conferenceId}/locations")
    public List<LocationVm> getAllCategoriesByConferenceId(@PathVariable Long conferenceId) {
        return locationService.getAllByConferenceId(conferenceId);
    }

    @PostMapping("/locations")
    public ResponseEntity<LocationVm> create(
            @Valid @RequestBody CreateLocationDto createLocationDto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var created = locationService.create(createLocationDto);
        var location = uriComponentsBuilder.path("/api/v1/locations/{id}").buildAndExpand(created.getId());
        return ResponseEntity
                .created(location.toUri())
                .body(created);
    }

    @PutMapping("/locations/{id}")
    public ResponseEntity<LocationVm> update(
            @PathVariable long id,
            @Valid @RequestBody UpdateLocationDto updateLocationDto
    ) {
        var updated = locationService.update(id, updateLocationDto);
        return ResponseEntity
                .ok()
                .body(updated);
    }

    @PutMapping("/locations/{locationId}/conferences/{conferenceId}")
    public ResponseEntity<Void> addConference(@PathVariable Long locationId, @PathVariable Long conferenceId) {
        conferenceService.addLocation(conferenceId, locationId);
        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/locations/{locationId}/conferences/{conferenceId}")
    public ResponseEntity<String> removeConference(@PathVariable long locationId, @PathVariable long conferenceId) {
        conferenceService.removeLocation(conferenceId, locationId);
        return ResponseEntity
                .ok()
                .body("deleted");
    }

    @DeleteMapping("/locations/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        locationService.delete(id);
        return ResponseEntity
                .ok()
                .body("deleted");
    }
}
