package ru.management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    @Positive
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    @Pattern(regexp = "\\d{6}", message = "Passport number must be 6 digits")
    private String passportNumber;
    @NotNull
    private Date passportDate;
    @NotNull
    private Integer salary;
}