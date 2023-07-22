package com.example.conference.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// Internal representation
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Talk extends BaseEntity {
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Talk(Long id) {
        this.id = id;
    }
}
