package com.example.democrud.crud.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequestDto {

    @NotBlank(message = "Email can not blank")
    private String email;
    @NotBlank(message = "Firstname can not blank")
    private String firstName;
    @NotBlank(message = "Lastname can not blank")
    private String lastName;

}
