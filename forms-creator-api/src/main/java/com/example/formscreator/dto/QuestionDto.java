package com.example.formscreator.dto;

import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {


    @Digits(fraction = 0, integer = 3, message = "Incorrect Number Format For OrderNumber")
    @Positive(message = "OrderNumber Must Be Positive")
    private Long orderNumber;

    @NotBlank(message = "Title Cannot Be Empty")
    private String title;

    @NotBlank(message = "Description Cannot Be Empty")
    private String description;

    @NotBlank(message = "Type Cannot Be Empty")
    private String type;

    @NotNull(message = "Required Not Provided")
    private boolean required;

    private List<String> options;

    private Long max;

    private Long min;

}
