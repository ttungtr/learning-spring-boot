package com.example.democrud.crud.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)

public class NotificationRequestDto {
    @NotBlank(message = "Title is not blank")
    private String title;

    @NotBlank(message = "Message is not blank")
    private String message;

    @NotBlank(message = "Detail is not blank")
    private String detail;

}
