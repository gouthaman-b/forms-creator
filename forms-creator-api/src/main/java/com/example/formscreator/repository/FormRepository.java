package com.example.formscreator.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.formscreator.entity.Form;
import com.example.formscreator.entity.User;

public interface FormRepository extends JpaRepository<Form, Integer>{
    List<Form> findByUser(User user);
    Optional<Form> findById(Long id);
    void deleteById(Long id);

    @Query("SELECT f FROM Form f WHERE f.user.id = :userId")
    List<Form> findByUserId(@Param("userId") Long userId);
}
