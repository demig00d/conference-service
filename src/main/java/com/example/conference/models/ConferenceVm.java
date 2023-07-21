package com.example.conference.models;

import com.example.conference.entities.Conference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConferenceVm {
    private Long id;
    private String title;
    private Date created;
    private Date updated;

    public static ConferenceVm fromEntity(Conference conference) {
        return new ConferenceVm(
                conference.getId(),
                conference.getTitle(),
                conference.getCreated(),
                conference.getUpdated()
        );
    }
}

