package com.Dockerappointment.service;

import com.Dockerappointment.dto.AvailabilityDto;
import com.Dockerappointment.dto.DoctorDto;
import com.Dockerappointment.entity.Doctor;
import com.Dockerappointment.entity.DoctorAvailability;
import com.Dockerappointment.entity.Role;
//import com.Dockerappointment.entity.User;
import com.Dockerappointment.exception.BadRequestException;
import com.Dockerappointment.exception.ResourceNotFoundException;
import com.Dockerappointment.repository.DoctorAvailabilityRepository;
import com.Dockerappointment.repository.DoctorRepository;
import com.Dockerappointment.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.Dockerappointment.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;
    private final UserRepository userRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             DoctorAvailabilityRepository doctorAvailabilityRepository,
                             UserRepository userRepository) {
        this.doctorRepository = doctorRepository;
        this.doctorAvailabilityRepository = doctorAvailabilityRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorDto getDoctorProfile(Long userId) {
        Doctor doctor = doctorRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found for user ID: " + userId));
        return convertToDto(doctor);
    }

    @Override
    @Transactional
    public DoctorDto updateDoctorProfile(Long userId, DoctorDto doctorDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User account not found with ID: " + userId));

        Doctor doctor = doctorRepository.findById(userId).orElse(new Doctor());

        doctor.setUser(user);
        doctor.setSpecialization(doctorDto.getSpecialization());
        doctor.setQualification(doctorDto.getQualification());
        doctor.setExperienceYears(doctorDto.getExperienceYears());
        doctor.setHospitalName(doctorDto.getHospitalName());
        doctor.setClinicAddress(doctorDto.getClinicAddress());
        doctor.setLanguages(doctorDto.getLanguages());
        doctor.setConsultationFee(doctorDto.getConsultationFee());
        doctor.setBiography(doctorDto.getBiography());

        if (doctorDto.getFullName() != null && !doctorDto.getFullName().isBlank()) {
            user.setFullName(doctorDto.getFullName());
            userRepository.save(user);
        }

        Doctor savedDoctor = doctorRepository.save(doctor);
        return convertToDto(savedDoctor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getAllApprovedDoctors() {
        return doctorRepository.findByIsApprovedTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> searchDoctors(String query) {
        return doctorRepository.searchDoctors(query).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AvailabilityDto addAvailability(Long doctorId, AvailabilityDto availabilityDto) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found with ID: " + doctorId));

        if (availabilityDto.getStartTime().isAfter(availabilityDto.getEndTime())) {
            throw new BadRequestException("Start time window cannot happen after the selected end time.");
        }

        DoctorAvailability availability = DoctorAvailability.builder()
                .doctor(doctor)
                .availableDate(availabilityDto.getAvailableDate())
                .startTime(availabilityDto.getStartTime())
                .endTime(availabilityDto.getEndTime())
                .isBooked(false)
                .build();

        DoctorAvailability saved = doctorAvailabilityRepository.save(availability);
        return convertToAvailabilityDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailabilityDto> getAvailableSlots(Long doctorId) {
        return doctorAvailabilityRepository.findByDoctorIdAndIsBookedFalse(doctorId).stream()
                .map(this::convertToAvailabilityDto)
                .collect(Collectors.toList());
    }

    private DoctorDto convertToDto(Doctor doctor) {
        return DoctorDto.builder()
                .id(doctor.getId())
                .fullName(doctor.getUser().getFullName())
                .email(doctor.getUser().getEmail())
                .specialization(doctor.getSpecialization())
                .qualification(doctor.getQualification())
                .experienceYears(doctor.getExperienceYears())
                .hospitalName(doctor.getHospitalName())
                .clinicAddress(doctor.getClinicAddress())
                .languages(doctor.getLanguages())
                .consultationFee(doctor.getConsultationFee())
                .biography(doctor.getBiography())
                .isApproved(doctor.getIsApproved())
                .rating(doctor.getRating())
                .totalReviews(doctor.getTotalReviews())
                .build();
    }

    private AvailabilityDto convertToAvailabilityDto(DoctorAvailability availability) {
        return AvailabilityDto.builder()
                .id(availability.getId())
                .doctorId(availability.getDoctor().getId())
                .availableDate(availability.getAvailableDate())
                .startTime(availability.getStartTime())
                .endTime(availability.getEndTime())
                .isBooked(availability.getIsBooked())
                .build();
    }
}