package ru.t1.java.demo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.model.user.User;
import ru.t1.java.demo.service.UserService;


@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(final String email) {
        User user = userService.getByEmail(email);
        return JwtEntityFactory.create(user);
    }

}
