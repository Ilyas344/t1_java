package ru.t1.java.demo.api.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.java.demo.api.UserApi;
import ru.t1.java.demo.model.dto.user.UserDto;
import ru.t1.java.demo.service.UserService;

import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;



    public ResponseEntity<UserDto> update(final UserDto dto) {
        log.info("UserController.update");
        return ResponseEntity.ok( userService.update(dto));
    }


    public ResponseEntity<UserDto> getById(final UUID id) {
        log.info("UserController.getById");
        return ResponseEntity.ok(userService.getById(id));
    }


    public ResponseEntity<Void> deleteById(final UUID id) {
        log.info("UserController.deleteById");
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
