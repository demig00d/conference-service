package com.example.conference.models.viewmodels;

import com.example.conference.entities.Talk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TalkVm {
    private Long id;
    private String title;
    private String description;
    private Long conferenceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Date created;
    private Date updated;

    public static TalkVm fromEntity(Talk talk) {
        return new TalkVm(
                talk.getId(),
                talk.getTitle(),
                talk.getDescription(),
                talk.getConference().getId(),
                talk.getStartTime(),
                talk.getEndTime(),
                talk.getCreated(),
                talk.getUpdated()
        );
    }

}

