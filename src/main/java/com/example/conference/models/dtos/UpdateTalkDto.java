package com.example.conference.models.dtos;

import com.example.conference.entities.Conference;
import com.example.conference.entities.Talk;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTalkDto {
    @Size(max = 64)
    private String title;
    private String description;
    private Conference conference;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public void updateEntity(Talk talk){
        talk.setTitle(this.title);
        talk.setDescription(this.description);
        talk.setConference(this.conference);
        talk.setStartTime(this.startTime);
        talk.setEndTime(this.endTime);
    }
}


