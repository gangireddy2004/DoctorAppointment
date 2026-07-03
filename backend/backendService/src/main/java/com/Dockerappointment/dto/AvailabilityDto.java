package com.Dockerappointment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailabilityDto {

    private Long id;
    private Long doctorId;

    @NotNull(message = "Available calendar date is required")
    private LocalDate availableDate;

    @NotNull(message = "Starting slot block window time is required")
    private LocalTime startTime;

    @NotNull(message = "Concluding slot block window time is required")
    private LocalTime endTime;

    private Boolean isBooked;
}