package com.example.conference.models.dtos;

import com.example.conference.entities.Conference;
import com.example.conference.entities.Talk;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateConferenceDto  {
    @NotBlank
    @Size(max = 64)
    private String title;
    private String description;
    private Set<Talk> talks;
    private LocalDate startDate;
    private LocalDate endDate;



    public Conference toEntity(){
        return new Conference(title, description, talks, startDate, endDate);
    }

}