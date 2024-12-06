package com.dococ.appointment.services.impl;

import com.dococ.appointment.dao.AppointmentDao;
import com.dococ.appointment.dto.PaginatedAppointmentDTO;
import com.dococ.appointment.entity.Appointment;
import com.dococ.appointment.enums.Status;
import com.dococ.appointment.exceptions.IllegalDataException;
import com.dococ.appointment.exceptions.ResourceNotFoundException;
import com.dococ.appointment.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    AppointmentDao appointmentDao;

    @Override
    public Appointment createAppointment(Appointment appointment) {
        ValidateAppointment(appointment);
        appointment.setStatus(Status.SCHEDULED.toString());

        return appointmentDao.save(appointment);
    }

    public static void isValidStatus(Appointment appointment, String status) {
        try {
            Status.valueOf(status);
            if (appointment.getStatus().equals(Status.CANCELED.toString()) && !status.equals(Status.CANCELED.toString())) {
                throw new IllegalDataException("Invalid status code");
            }

            if (appointment.getStatus().equals(Status.COMPLETED.toString()) && !status.equals(Status.COMPLETED.toString())) {
                throw new IllegalDataException("Invalid status code");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalDataException("Invalid status code");
        }

    }

    @Override
    public Appointment updateAppointmentStatus(int id, Appointment appointment) {
        Optional<Appointment> savedAppointment = appointmentDao.findById(id);
        if (savedAppointment.isPresent()) {
            isValidStatus(savedAppointment.get(), appointment.getStatus());

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
        if (offset < 0) {
            throw new IllegalDataException("offset cannot be negative");
        }

        if (size < 0) {
            throw new IllegalDataException("size cannot be negative");
        }

        if (size > 200) {
            throw new IllegalDataException("size cannot be more than 200");
        }

        Pageable pageable = PageRequest.of(offset, size, Sort.by(Sort.Order.asc("id")));
        return convertPaginatedDataToDTO(appointmentDao.findByPatientId(id, pageable));
    }

    @Override
    public PaginatedAppointmentDTO getDoctorAppointments(int id, int offset, int size) {
        if (offset < 0) {
            throw new IllegalDataException("offset cannot be negative");
        }

        if (size < 0) {
            throw new IllegalDataException("size cannot be negative");
        }

        if (size > 200) {
            throw new IllegalDataException("size cannot be more than 200");
        }

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

    private void ValidateAppointment(Appointment appointment) {
        if (appointment.getAppointmentDate() == null) {
            throw new IllegalDataException("Invalid appointment date");
        }

        if (appointment.getAppointmentTime() == null) {
            throw new IllegalDataException("Invalid appointment time");
        }

        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        if (appointment.getAppointmentDate().isBefore(currentDate) || appointment.getAppointmentTime().isBefore(currentTime)) {
            throw new IllegalDataException("Invalid appointment date or time, appointment cannot be in the past");
        }

        if (appointment.getPatientSymptom() == null || appointment.getPatientSymptom().isBlank()) {
            throw new IllegalDataException("Invalid symptom value");
        }

        if (appointment.getDoctorId() < 1) {
            throw new IllegalDataException("Invalid doctor id value");
        }

        if (appointment.getPatientId() < 1) {
            throw new IllegalDataException("Invalid patient id value");
        }
    }
}
