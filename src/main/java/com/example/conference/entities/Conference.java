package com.example.conference.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

// Internal representation
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Conference extends BaseEntity {
  private String title;
  private String description;
  private LocalDate startDate;
  private LocalDate endDate;

  public Conference(Long id) {
    this.id = id;
  }
}
