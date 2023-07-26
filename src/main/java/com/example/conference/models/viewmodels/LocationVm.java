package com.example.conference.models.viewmodels;

import com.example.conference.entities.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationVm {
    private Long id;
    private String title;
    private Date created;
    private Date updated;

    public static LocationVm fromEntity(Location location) {
        return new LocationVm(
                location.getId(),
                location.getTitle(),
                location.getCreated(),
                location.getUpdated()
        );
    }
}
