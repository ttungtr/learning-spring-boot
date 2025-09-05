package com.example.democrud.crud.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationResponseDto {

    private Long id;

    private String title;

    private String message;

    private String detail;

    private Long userId;

    private Boolean isRead ;


}
