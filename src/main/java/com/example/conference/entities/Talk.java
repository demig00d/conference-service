package com.example.conference.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
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
    @ManyToOne(fetch = FetchType.LAZY)
    private Conference conference;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Talk(Long id) {
        this.id = id;
    }
}
