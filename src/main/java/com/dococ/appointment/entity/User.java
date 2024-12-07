package com.dococ.appointment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {


    public int userId;

    public String name;

    public String email;

    private String role;
}
