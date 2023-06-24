package com.example.formscreator.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.formscreator.entity.Option;

public interface OptionRepository extends JpaRepository<Option, Integer>{

}
