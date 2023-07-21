package com.example.conference.models;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePartiallyConferenceDto  {
    @Size(max = 64)
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}


