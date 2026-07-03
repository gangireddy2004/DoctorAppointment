package com.Dockerappointment.controller;

import com.Dockerappointment.dto.AppointmentDto;
import com.Dockerappointment.security.UserDetailsImpl;
import com.Dockerappointment.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/book")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentDto> bookAppointment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody AppointmentDto appointmentDto) {
        AppointmentDto created = appointmentService.bookAppointment(userDetails.getId(), appointmentDto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/patient")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<AppointmentDto>> getPatientHistory(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(appointmentService.getPatientAppointments(userDetails.getId()));
    }

    @GetMapping("/doctor")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<AppointmentDto>> getDoctorSchedule(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(userDetails.getId()));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<AppointmentDto> updateStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id,
            @RequestParam String status) {
        AppointmentDto updated = appointmentService.updateAppointmentStatus(userDetails.getId(), id, status);
        return ResponseEntity.ok(updated);
    }
}