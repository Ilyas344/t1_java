package ru.t1.java.demo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.demo.model.dto.user.UserDto;

import java.util.UUID;


@RequestMapping("/api/v1/")
@Validated
@Tag(name = "User Controller", description = "User API")
public interface UserApi {
    @Operation(summary = "Обновление user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные user обновлены",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)))

    })
    @PutMapping("users")
    ResponseEntity<UserDto> update(@RequestBody UserDto dto);

    @Operation(summary = "Получить user по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные user получены",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)))

    })
    @GetMapping("users/{id}")
    ResponseEntity<UserDto> getById(@PathVariable UUID id);

    @Operation(summary = "Удаление user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные user удалены",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)))

    })

    @DeleteMapping("users/{id}")
    ResponseEntity<Void> deleteById(@PathVariable final UUID id);


}
