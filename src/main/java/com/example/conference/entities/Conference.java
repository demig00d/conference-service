package com.example.conference.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
}
