package com.example.formscreator.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormDto {
    @NotBlank(message = "Title Cannot Be Empty")
    @Size(min=1)
    String title;
    @NotBlank(message = "Description Cannot Be Empty")
    String description;
    @NotNull(message = "Enabled Status Cannot Be Empty")
    boolean enabled;

}
