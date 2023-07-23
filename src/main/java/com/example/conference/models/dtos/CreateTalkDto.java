package com.example.conference.models.dtos;

import com.example.conference.entities.Conference;
import com.example.conference.entities.Talk;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTalkDto  {
    @NotBlank
    @Size(max = 64)
    private String title;

    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Talk toEntity(Conference conference) {
        return new Talk(
                this.title,
                this.description,
                conference,
                this.startTime,
                this.endTime
        );
    }

}
