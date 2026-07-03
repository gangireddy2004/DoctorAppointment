package com.Dockerappointment.controller;

import com.Dockerappointment.dto.PatientProfileDto;
import com.Dockerappointment.security.UserDetailsImpl;
import com.Dockerappointment.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<PatientProfileDto> getMyProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        PatientProfileDto profile = patientService.getPatientProfile(userDetails.getId());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientProfileDto> updateMyProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody PatientProfileDto profileDto) {
        PatientProfileDto updatedProfile = patientService.createOrUpdateProfile(userDetails.getId(), profileDto);
        return ResponseEntity.ok(updatedProfile);
    }
}