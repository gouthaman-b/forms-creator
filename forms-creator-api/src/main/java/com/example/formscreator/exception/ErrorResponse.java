package com.example.formscreator.exception;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int statusCode;
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss a")
    private Date timestamp;
    private String exceptionType;
    private String message;
}
