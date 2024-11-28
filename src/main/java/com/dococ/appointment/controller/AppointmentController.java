package com.dococ.appointment.controller;

import com.dococ.appointment.dto.PaginatedAppointmentDTO;
import com.dococ.appointment.entity.Appointment;
import com.dococ.appointment.services.AppointmentService;
import com.dococ.appointment.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class AppointmentController {

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/appointment")
    ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment, @RequestHeader String authorization) {

        authenticationService.validateRole(authorization, List.of("PATIENT"));

        Appointment createdAppointment = appointmentService.createAppointment(appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAppointment);
    }

    @PatchMapping("/appointment/{id}")
    ResponseEntity<Appointment> updateAppointmentStatus(@PathVariable int id, @RequestBody Appointment appointment, @RequestHeader String authorization) {
        authenticationService.validateRole(authorization, List.of("PATIENT", "DOCTOR"));

        Appointment updatedAppointment = appointmentService.updateAppointmentStatus(id, appointment);
        return ResponseEntity.ok().body(updatedAppointment);
    }

    @GetMapping("/patient/{id}/appointment")
    ResponseEntity<PaginatedAppointmentDTO> getPatientsAppointment(@PathVariable int id, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "20") int size, @RequestHeader String authorization) {
        authenticationService.validateRole(authorization, List.of("PATIENT", "ADMIN"));

        PaginatedAppointmentDTO appointments = appointmentService.getPatientAppointments(id, offset, size);
        return ResponseEntity.ok().body(appointments);
    }

    @GetMapping("/doctor/{id}/appointment")
    ResponseEntity<PaginatedAppointmentDTO> getDoctorsAppointment(@PathVariable int id, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "20") int size, @RequestHeader String authorization) {
        authenticationService.validateRole(authorization, List.of("DOCTOR", "ADMIN"));

        PaginatedAppointmentDTO appointments = appointmentService.getDoctorAppointments(id, offset, size);
        return ResponseEntity.ok().body(appointments);
    }

    @GetMapping("/appointment/{id}")
    ResponseEntity<Appointment> getAppointmentById(@PathVariable int id, @RequestHeader String authorization) {
        authenticationService.validateRole(authorization, List.of("PATIENT", "DOCTOR", "ADMIN"));

        Appointment appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok().body(appointment);
    }
}
