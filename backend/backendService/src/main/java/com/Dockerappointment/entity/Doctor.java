package com.Dockerappointment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank(message = "Specialization is required")
    @Column(nullable = false, length = 100)
    private String specialization;

    @NotBlank(message = "Qualification is required")
    @Column(nullable = false, length = 100)
    private String qualification;

    @NotNull(message = "Experience years is required")
    @Column(nullable = false)
    private Integer experienceYears;

    @NotBlank(message = "Hospital affiliation is required")
    @Column(nullable = false, length = 150)
    private String hospitalName;

    @NotBlank(message = "Clinic address is required")
    @Column(nullable = false, length = 255)
    private String clinicAddress;

    @NotBlank(message = "Consultation languages are required")
    @Column(nullable = false, length = 100)
    private String languages;

    @NotNull(message = "Consultation fee is required")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal consultationFee;

    @Column(columnDefinition = "TEXT")
    private String biography;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isApproved = false;

    @Builder.Default
    private Double rating = 0.0;

    @Builder.Default
    private Integer totalReviews = 0;
}