package com.example.conference.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

// Internal representation
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Conference extends BaseEntity {
  private String title;
  private String description;

  @OneToMany(mappedBy = "conference")
  private Set<Talk> talks = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
          name = "conference_location",
          joinColumns = @JoinColumn(name = "conference_id", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(name = "location_id", referencedColumnName = "id")
  )
  private Set<Location> locations = new HashSet<>();
  private LocalDate startDate;
  private LocalDate endDate;

  public Conference(Long id) {
    this.id = id;
  }

  public void addTalk(Talk talk) {
    talks.add(talk);
    talk.setConference(this);
  }

  public void removeTalk(Talk talk) {
    talks.remove(talk);
    talk.setConference(null);
  }

  public void addLocation(Location location) {
    locations.add(location);
    location.getConferences().add(this);
  }

  public void removeLocation(Location location) {
    locations.remove(location);
    location.getConferences().remove(this);
  }

}
