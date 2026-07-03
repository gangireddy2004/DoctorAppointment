package com.Dockerappointment.service;

import com.Dockerappointment.dto.AvailabilityDto;
import com.Dockerappointment.dto.DoctorDto;

import java.util.List;

public interface DoctorService {
    DoctorDto getDoctorProfile(Long userId);
    DoctorDto updateDoctorProfile(Long userId, DoctorDto doctorDto);
    List<DoctorDto> getAllApprovedDoctors();
    List<DoctorDto> searchDoctors(String query);

    // Availability Schedule Slotted Actions
    AvailabilityDto addAvailability(Long doctorId, AvailabilityDto availabilityDto);
    List<AvailabilityDto> getAvailableSlots(Long doctorId);
}