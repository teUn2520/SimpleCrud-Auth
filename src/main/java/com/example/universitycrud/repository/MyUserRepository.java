package com.example.universitycrud.repository;

import com.example.universitycrud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyUserRepository extends JpaRepository<User, Long> {
    Optional<User> findUsersByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
}
