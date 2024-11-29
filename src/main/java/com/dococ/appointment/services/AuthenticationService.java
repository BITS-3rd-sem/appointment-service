package com.dococ.appointment.services;

import java.util.List;

public interface AuthenticationService {

    void validateRole(String token, List<String> roles);
}
