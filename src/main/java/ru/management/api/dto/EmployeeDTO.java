package ru.management.api.dto;

import com.fasterxml.jackson.annotation.JsonMerge;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    @Positive
    private Long id;
    @NotBlank
    @JsonMerge
    private String name;
    @NotBlank
    @JsonMerge
    private String surname;
    @NotBlank
    @Pattern(regexp = "\\d{6}", message = "Passport number must be 6 digits")
    @JsonMerge
    private String passportNumber;
    @NotNull
    @JsonMerge
    private Date passportDate;
    @NotNull
    @JsonMerge
    @Positive
    private BigDecimal salary;
}