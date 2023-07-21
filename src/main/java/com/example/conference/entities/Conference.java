package com.example.conference.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Internal representation
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Conference extends BaseEntity {
  private String title;

  public Conference(Long id) {
    this.id = id;
  }
}
