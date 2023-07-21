package com.example.conference.models;

import com.example.conference.entities.Conference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateConferenceDto  {
    @NotBlank
    @Size(max = 64)
    private String title;

    private String description;
    private LocalDate startDate;
    private LocalDate endDate;



    public Conference toEntity(){
        return new Conference(title, description, startDate, endDate);
    }

}