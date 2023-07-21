package com.example.conference.models;

import com.example.conference.entities.Conference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateConferenceDto  {
    @NotBlank
    @Size(max = 64)
    private String title;

    public Conference toEntity(){
        return new Conference(title);
    }

}