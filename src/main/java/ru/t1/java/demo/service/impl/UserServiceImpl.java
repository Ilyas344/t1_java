package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.exception.UserExistsException;
import ru.t1.java.demo.mapper.UserMapper;
import ru.t1.java.demo.model.dto.user.UserDto;
import ru.t1.java.demo.model.user.Role;
import ru.t1.java.demo.model.user.User;
import ru.t1.java.demo.repository.UserRepository;
import ru.t1.java.demo.service.UserService;


import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMap;


    @Override
    public UserDto getById(final UUID id) {
        User user =userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));
        return userMap.userMapper(user);
    }
    @Override
    public User getUserById(final UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));
    }

    @Override
    public User getByUsername(final String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));
    }
    @Override
    public User getByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));
    }

    @Override
    public UserDto update(final UserDto newUser) {
        User oldUser = userRepository.findById(newUser.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));
        oldUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepository.save(
                userMap.updateUserFromDto(newUser,oldUser));
        return userMap.userMapper(oldUser);

    }


    @Override
    public UserDto create(final UserDto userDto) {
        User user = userMap.userDtoMapper(userDto);

        if(userRepository.existsByUser(user.getUsername(),user.getEmail())){
            throw new UserExistsException("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.ROLE_USER));
        return userMap.userMapper(userRepository.save(user));
    }


    @Override
    public void delete(final UUID id) {
        userRepository.deleteById(id);
    }

}
