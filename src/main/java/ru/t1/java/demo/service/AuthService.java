package ru.t1.java.demo.service;


import ru.t1.java.demo.model.dto.auth.JwtRequest;
import ru.t1.java.demo.model.dto.auth.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);

}
