package com.example.formscreator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.formscreator.entity.Response;

public interface ResponseRepository extends JpaRepository<Response, Integer>{
    
}
