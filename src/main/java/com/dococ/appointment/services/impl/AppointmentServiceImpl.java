package com.dococ.appointment.services.impl;

import com.dococ.appointment.dao.AppointmentDao;
import com.dococ.appointment.dto.PaginatedAppointmentDTO;
import com.dococ.appointment.enums.Status;
import com.dococ.appointment.entity.Appointment;
import com.dococ.appointment.exceptions.IllegalDataException;
import com.dococ.appointment.exceptions.ResourceNotFoundException;
import com.dococ.appointment.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    AppointmentDao appointmentDao;

    @Override
    public Appointment createAppointment(Appointment appointment) {
        appointment.setStatus(Status.SCHEDULED.toString());

        return appointmentDao.save(appointment);
    }

    public static void isValidStatus(String status) {
        try {
            Status.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalDataException("Invalid status code");
        }
    }

    @Override
    public Appointment updateAppointmentStatus(int id, Appointment appointment) {
        Optional<Appointment> savedAppointment = appointmentDao.findById(id);
        if (savedAppointment.isPresent()) {
            isValidStatus(appointment.getStatus().toString());

            Appointment updatedAppointment = savedAppointment.get();
            updatedAppointment.setStatus(appointment.getStatus());
            return appointmentDao.save(updatedAppointment);
        } else {
            throw new ResourceNotFoundException("Appointment doesn't exist");
        }
    }

    @Override
    public Appointment getAppointmentById(int id) {
        Optional<Appointment> savedAppointment = appointmentDao.findById(id);
        if (savedAppointment.isPresent()) {
            return savedAppointment.get();
        } else {
            throw new ResourceNotFoundException("Appointment doesn't exist");
        }
    }

    @Override
    public PaginatedAppointmentDTO getPatientAppointments(int id, int offset, int size) {
        Pageable pageable = PageRequest.of(offset, size, Sort.by(Sort.Order.asc("id")));
        return convertPaginatedDataToDTO(appointmentDao.findByPatientId(id, pageable));
    }

    @Override
    public PaginatedAppointmentDTO getDoctorAppointments(int id, int offset, int size) {
        Pageable pageable = PageRequest.of(offset, size, Sort.by(Sort.Order.asc("id")));
        return convertPaginatedDataToDTO(appointmentDao.findByDoctorId(id, pageable));
    }

    private PaginatedAppointmentDTO convertPaginatedDataToDTO(Page<Appointment> paginatedData) {
        PaginatedAppointmentDTO dto = new PaginatedAppointmentDTO();
        dto.setPageNumber(paginatedData.getPageable().getPageNumber());
        dto.setPageSize(paginatedData.getPageable().getPageSize());
        dto.setAppointments(paginatedData.getContent());
        dto.setTotalPages(paginatedData.getTotalPages());
        dto.setTotalAppointments((int) paginatedData.getTotalElements());

        return dto;
    }
}
