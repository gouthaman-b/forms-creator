package com.example.formscreator.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.formscreator.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

}
