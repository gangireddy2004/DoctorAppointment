package com.Dockerappointment.repository;

import com.Dockerappointment.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByIsApprovedTrue();

    @Query("SELECT d FROM Doctor d WHERE d.isApproved = true AND " +
            "(LOWER(d.specialization) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(d.hospitalName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(d.user.fullName) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Doctor> searchDoctors(@Param("query") String query);
}