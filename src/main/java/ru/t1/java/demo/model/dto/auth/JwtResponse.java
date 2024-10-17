package ru.t1.java.demo.model.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "Request after login")
public class JwtResponse {

    private UUID id;
    private String email;
    private String accessToken;
    private String refreshToken;

}
