package ru.t1.java.demo.service;


import ru.t1.java.demo.model.dto.user.UserDto;
import ru.t1.java.demo.model.user.User;

import java.util.UUID;

public interface UserService {

    UserDto getById(UUID id);
    User getUserById(UUID id);

    User getByUsername(String username);
    User getByEmail(String username);

    UserDto update(UserDto user);

    UserDto create(UserDto user);

    void delete(UUID id);

}
