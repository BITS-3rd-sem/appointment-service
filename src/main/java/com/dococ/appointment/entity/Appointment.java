package com.dococ.appointment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    int patientId;

    int doctorId;

    String patientSymptom;

    LocalDate appointmentDate;

    LocalTime appointmentTime;

    String status;
}
