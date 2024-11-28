package com.dococ.appointment.dao;

import com.dococ.appointment.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentDao extends JpaRepository<Appointment, Integer> {
    Page<Appointment> findByPatientId(int patientId, Pageable pageable);
    Page<Appointment> findByDoctorId(int doctorId, Pageable pageable);
}
