package ru.management.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {
    @NotBlank
    private String type;
    @NotBlank
    private String title;
    @Positive
    private Integer status;
    @NotBlank
    private String detail;
    @NotBlank
    private String instance;
}
