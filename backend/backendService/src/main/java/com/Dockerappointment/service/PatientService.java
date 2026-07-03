package com.Dockerappointment.service;

import com.Dockerappointment.dto.PatientProfileDto;

public interface PatientService {
    PatientProfileDto getPatientProfile(Long userId);
    PatientProfileDto createOrUpdateProfile(Long userId, PatientProfileDto profileDto);
}