package com.example.conference.models.viewmodels;

import com.example.conference.entities.Conference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConferenceVm {
    private Long id;
    private String title;
    private String description;
    private Set<TalkVm> talks;
    private LocalDate startDate;
    private LocalDate endDate;
    private Date created;
    private Date updated;

    public static ConferenceVm fromEntity(Conference conference) {
        if (conference.getTalks() == null)
                conference.setTalks(new HashSet<>());
        return new ConferenceVm(
                conference.getId(),
                conference.getTitle(),
                conference.getDescription(),
                conference.getTalks()
                        .stream()
                        .map(TalkVm::fromEntity)
                        .collect(Collectors.toSet()),
                conference.getStartDate(),
                conference.getEndDate(),
                conference.getCreated(),
                conference.getUpdated()
        );
    }
}

