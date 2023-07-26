package com.example.conference.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Location extends BaseEntity {
    private String title;

    @ManyToMany(mappedBy = "locations")
    private Set<Conference> conferences = new HashSet<>();

    public Location(String title) {
        this.title = title;
    }

    public void addConference(Conference conference) {
        conferences.add(conference);
        conference.getLocations().add(this);
    }

    public void removeConference(Conference conference) {
        conferences.remove(conference);
        conference.getLocations().remove(this);
    }
}
