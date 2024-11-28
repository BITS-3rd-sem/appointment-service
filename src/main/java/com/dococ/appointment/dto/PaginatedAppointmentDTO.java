package com.dococ.appointment.dto;

import com.dococ.appointment.entity.Appointment;
import lombok.Data;

import java.util.List;

@Data
public class PaginatedAppointmentDTO {
    int pageNumber;
    int pageSize;
    int totalPages;
    int totalAppointments;
    List<Appointment> appointments;
}
