package com.example.formscreator.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.formscreator.entity.Form;
import com.example.formscreator.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer>{
    List<Question> findByForm(Form form);
    @Modifying
    @Query("delete from Question t where t.id = ?1")
    void deleteById(Long id);
}
