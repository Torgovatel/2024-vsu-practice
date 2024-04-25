package ru.management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String city;
    @NotBlank
    private String street;
    @NotBlank
    private String region;
    @NotBlank
    @Pattern(regexp = "\\d{6}", message = "Postal code must be 6 digits")
    private String postalCode;
    @NotBlank
    @Pattern(regexp = "\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}", message = "Phone number must be in format +7(123)456-78-90")
    private String phoneNumber;
}