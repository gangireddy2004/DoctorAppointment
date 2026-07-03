package com.Dockerappointment.controller;

import com.Dockerappointment.dto.AvailabilityDto;
import com.Dockerappointment.dto.DoctorDto;
import com.Dockerappointment.security.UserDetailsImpl;
import com.Dockerappointment.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<DoctorDto> getDoctorProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        DoctorDto profile = doctorService.getDoctorProfile(userDetails.getId());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorDto> updateDoctorProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody DoctorDto doctorDto) {
        DoctorDto updatedProfile = doctorService.updateDoctorProfile(userDetails.getId(), doctorDto);
        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/directory")
    public ResponseEntity<List<DoctorDto>> getAllApprovedDoctors() {
        return ResponseEntity.ok(doctorService.getAllApprovedDoctors());
    }

    @GetMapping("/search")
    public ResponseEntity<List<DoctorDto>> searchDoctors(@RequestParam("query") String query) {
        return ResponseEntity.ok(doctorService.searchDoctors(query));
    }

    @PostMapping("/availability")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<AvailabilityDto> addAvailabilitySlot(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody AvailabilityDto availabilityDto) {
        AvailabilityDto savedSlot = doctorService.addAvailability(userDetails.getId(), availabilityDto);
        return ResponseEntity.ok(savedSlot);
    }

    @GetMapping("/{doctorId}/slots")
    public ResponseEntity<List<AvailabilityDto>> getOpenDoctorSlots(@PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorService.getAvailableSlots(doctorId));
    }
}