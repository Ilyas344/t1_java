package ru.t1.java.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.t1.java.demo.model.user.User;


import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(
            String username
    );

    Optional<User> findByUsername(
            String username
    );

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username and u.email=:email")
    Boolean existsByUser(String username, String email);


}
