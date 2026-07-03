package com.Dockerappointment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDto {

    private Long id;
    private String fullName;
    private String email;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotBlank(message = "Qualification is required")
    private String qualification;

    @NotNull(message = "Years of experience is required")
    private Integer experienceYears;

    @NotBlank(message = "Hospital affiliation name is required")
    private String hospitalName;

    @NotBlank(message = "Clinic setup address is required")
    private String clinicAddress;

    @NotBlank(message = "Spoken consultation languages are required")
    private String languages;

    @NotNull(message = "Consultation session fee is required")
    private BigDecimal consultationFee;

    private String biography;
    private Boolean isApproved;
    private Double rating;
    private Integer totalReviews;
}