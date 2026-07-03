package com.Dockerappointment.repository;

import com.Dockerappointment.entity.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {
    List<DoctorAvailability> findByDoctorIdAndIsBookedFalse(Long doctorId);
    List<DoctorAvailability> findByDoctorIdAndAvailableDate(Long doctorId, LocalDate date);
}