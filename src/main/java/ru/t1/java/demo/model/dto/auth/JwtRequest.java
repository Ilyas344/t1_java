package ru.t1.java.demo.model.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request for login")
public class JwtRequest {

    @Schema(
            description = "email",
            example = "johndoe@gmail.com"
    )
    @NotNull(
            message = "Username must be not null."
    )
    private String email;

    @Schema(
            description = "password",
            example = "12345"
    )
    @NotNull(
            message = "Password must be not null."
    )
    private String password;

}
