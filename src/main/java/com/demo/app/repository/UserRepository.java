package com.demo.app.repository;

import com.demo.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);

    Optional<User> findUserByLogin(String login);

    Boolean existsUserByLogin(String login);
}
