package com.example.formscreator.dto;

import javax.validation.constraints.NotNull;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {

    private Long id;
    
    @NotNull(message = "Answer Cannot Be Empty")
    private String answer;

    @NotNull(message = "Question Id Cannot Be Empty")
    private Long questionId;
}
