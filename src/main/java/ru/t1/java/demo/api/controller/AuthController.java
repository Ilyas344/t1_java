package ru.t1.java.demo.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.java.demo.api.AuthApi;
import ru.t1.java.demo.model.dto.auth.JwtRequest;
import ru.t1.java.demo.model.dto.auth.JwtResponse;
import ru.t1.java.demo.model.dto.user.UserDto;
import ru.t1.java.demo.service.AuthService;
import ru.t1.java.demo.service.UserService;


@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;
    private final UserService userService;


    public ResponseEntity<JwtResponse> login(final JwtRequest loginRequest) {
        log.info("AuthController.login");
        return ResponseEntity.ok(authService.login(loginRequest));
    }


    public ResponseEntity<UserDto> register(final UserDto userDto) {
        log.info("AuthController.register");
        return ResponseEntity.ok(userService.create(userDto));
    }

    public ResponseEntity<JwtResponse> refresh(final String refreshToken) {
        log.info("AuthController.refresh");
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }

}
