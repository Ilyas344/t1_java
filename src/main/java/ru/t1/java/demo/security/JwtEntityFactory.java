package ru.t1.java.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.t1.java.demo.model.security.AppUserDetails;
import ru.t1.java.demo.model.user.Role;
import ru.t1.java.demo.model.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtEntityFactory {

    public static AppUserDetails create(
            final User user
    ) {
        return new AppUserDetails(
                user,
                mapToGrantedAuthorities(new ArrayList<>(user.getRoles())));
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(
            final List<Role> roles
    ) {
        return roles.stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
