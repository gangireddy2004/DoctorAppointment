package com.Dockerappointment.service;

import com.Dockerappointment.dto.AppointmentDto;
import java.util.List;

public interface AppointmentService {
    AppointmentDto bookAppointment(Long patientId, AppointmentDto appointmentDto);
    List<AppointmentDto> getPatientAppointments(Long patientId);
    List<AppointmentDto> getDoctorAppointments(Long doctorId);
    AppointmentDto updateAppointmentStatus(Long doctorId, Long appointmentId, String status);
}