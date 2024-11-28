package com.dococ.appointment.services;

import com.dococ.appointment.dto.PaginatedAppointmentDTO;
import com.dococ.appointment.entity.Appointment;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface AppointmentService {

    Appointment createAppointment(Appointment appointment);
    Appointment updateAppointmentStatus(int id, Appointment appointment);
    Appointment getAppointmentById(int id);
    PaginatedAppointmentDTO getPatientAppointments(int id, int offset, int size);
    PaginatedAppointmentDTO getDoctorAppointments(int id, int offset, int size);
}
