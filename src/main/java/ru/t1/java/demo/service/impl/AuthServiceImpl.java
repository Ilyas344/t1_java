package ru.t1.java.demo.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.mapper.JwtMapper;
import ru.t1.java.demo.model.dto.auth.JwtRequest;
import ru.t1.java.demo.model.dto.auth.JwtResponse;
import ru.t1.java.demo.model.user.User;
import ru.t1.java.demo.security.JwtTokenProvider;
import ru.t1.java.demo.service.AuthService;
import ru.t1.java.demo.service.UserService;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtMapper jwtMapper;

    @Override
    public JwtResponse login(final JwtRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword())
        );
        User user = userService.getByEmail(loginRequest.getEmail());
        JwtResponse jwtResponse =jwtMapper.toDto(user);
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(
                user.getId(), user.getEmail(), user.getRoles()));
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(
                user.getId(), user.getEmail()));
        return jwtResponse;
    }

    @Override
    public JwtResponse refresh(final String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }

}
