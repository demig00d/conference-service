package com.example.conference.models.viewmodels;

import com.example.conference.entities.Conference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConferenceVm {
    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Date created;
    private Date updated;

    public static ConferenceVm fromEntity(Conference conference) {
        return new ConferenceVm(
                conference.getId(),
                conference.getTitle(),
                conference.getDescription(),
                conference.getStartDate(),
                conference.getEndDate(),
                conference.getCreated(),
                conference.getUpdated()
        );
    }
}

