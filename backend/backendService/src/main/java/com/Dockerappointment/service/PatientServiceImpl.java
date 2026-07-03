package com.Dockerappointment.service;

import com.Dockerappointment.dto.PatientProfileDto;
import com.Dockerappointment.entity.Patient;
import com.Dockerappointment.entity.Role;
import com.Dockerappointment.entity.User;
import com.Dockerappointment.exception.ResourceNotFoundException;
import com.Dockerappointment.repository.PatientRepository;
import com.Dockerappointment.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PatientServiceImpl(PatientRepository patientRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PatientProfileDto getPatientProfile(Long userId) {
        Patient patient = patientRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found for user ID: " + userId));

        return convertToDto(patient);
    }

    @Override
    @Transactional
    public PatientProfileDto createOrUpdateProfile(Long userId, PatientProfileDto profileDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User account not found with ID: " + userId));

        Patient patient = patientRepository.findById(userId).orElse(new Patient());

        patient.setUser(user);
        patient.setPhoneNumber(profileDto.getPhoneNumber());
        patient.setDateOfBirth(profileDto.getDateOfBirth());
        patient.setGender(profileDto.getGender());
        patient.setBloodGroup(profileDto.getBloodGroup());
        patient.setAddress(profileDto.getAddress());

        // Update name in parent user entity if modified
        if (profileDto.getFullName() != null && !profileDto.getFullName().isBlank()) {
            user.setFullName(profileDto.getFullName());
            userRepository.save(user);
        }

        Patient savedPatient = patientRepository.save(patient);
        return convertToDto(savedPatient);
    }

    private PatientProfileDto convertToDto(Patient patient) {
        return PatientProfileDto.builder()
                .id(patient.getId())
                .fullName(patient.getUser().getFullName())
                .email(patient.getUser().getEmail())
                .phoneNumber(patient.getPhoneNumber())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .bloodGroup(patient.getBloodGroup())
                .address(patient.getAddress())
                .build();
    }
}