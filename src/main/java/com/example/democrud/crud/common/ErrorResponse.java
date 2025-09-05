package com.example.democrud.crud.common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path; //

    public ErrorResponse () {
        this.timestamp = LocalDateTime.now();
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.error = "Internal Server Error";
        this.message = "Internal Server Error";
        this.path = "/";
    }
    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }






}
