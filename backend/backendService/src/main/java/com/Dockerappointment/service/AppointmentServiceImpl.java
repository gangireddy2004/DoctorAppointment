package com.Dockerappointment.service;

import com.Dockerappointment.dto.AppointmentDto;
import com.Dockerappointment.entity.Appointment;
import com.Dockerappointment.entity.Doctor;
import com.Dockerappointment.entity.DoctorAvailability;
import com.Dockerappointment.entity.Patient;
import com.Dockerappointment.exception.BadRequestException;
import com.Dockerappointment.exception.ResourceNotFoundException;
import com.Dockerappointment.repository.AppointmentRepository;
import com.Dockerappointment.repository.DoctorAvailabilityRepository;
import com.Dockerappointment.repository.DoctorRepository;
import com.Dockerappointment.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorAvailabilityRepository availabilityRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  PatientRepository patientRepository,
                                  DoctorRepository doctorRepository,
                                  DoctorAvailabilityRepository availabilityRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.availabilityRepository = availabilityRepository;
    }

    @Override
    @Transactional
    public AppointmentDto bookAppointment(Long patientId, AppointmentDto appointmentDto) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found."));

        DoctorAvailability slot = availabilityRepository.findById(appointmentDto.getAvailabilitySlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Selected availability slot not found."));

        if (slot.getIsBooked()) {
            throw new BadRequestException("This availability time slot is already booked.");
        }

        Doctor doctor = slot.getDoctor();

        // Reserve the slot immediately
        slot.setIsBooked(true);
        availabilityRepository.save(slot);

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(slot.getAvailableDate())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .status("PENDING")
                .build();

        Appointment saved = appointmentRepository.save(appointment);
        return convertToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDto> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDto> getDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AppointmentDto updateAppointmentStatus(Long doctorId, Long appointmentId, String status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment record not found."));

        if (!appointment.getDoctor().getId().equals(doctorId)) {
            throw new BadRequestException("Unauthorized to modify this appointment.");
        }

        String normalizedStatus = status.toUpperCase();
        appointment.setStatus(normalizedStatus);

        // Generate dynamic Jitsi URL if the doctor approves
        if ("CONFIRMED".equals(normalizedStatus)) {
            String uniqueRoom = "HealthBridge-" + UUID.randomUUID().toString().substring(0, 13);
            appointment.setVirtualMeetingUrl("https://meet.jit.si/" + uniqueRoom);
        }

        Appointment updated = appointmentRepository.save(appointment);
        return convertToDto(updated);
    }

    private AppointmentDto convertToDto(Appointment appt) {
        return AppointmentDto.builder()
                .id(appt.getId())
                .patientId(appt.getPatient().getId())
                .patientName(appt.getPatient().getUser().getFullName())
                .doctorId(appt.getDoctor().getId())
                .doctorName(appt.getDoctor().getUser().getFullName())
                .specialization(appt.getDoctor().getSpecialization())
                .appointmentDate(appt.getAppointmentDate())
                .startTime(appt.getStartTime())
                .endTime(appt.getEndTime())
                .status(appt.getStatus())
                .virtualMeetingUrl(appt.getVirtualMeetingUrl())
                .build();
    }
}