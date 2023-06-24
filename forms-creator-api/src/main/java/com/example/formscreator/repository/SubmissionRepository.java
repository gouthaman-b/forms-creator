package com.example.formscreator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.formscreator.entity.Form;
import com.example.formscreator.entity.Submission;
import java.util.List;
import java.util.Optional;


public interface SubmissionRepository extends JpaRepository<Submission, Integer>{
    List<Submission> findByForm(Form form);
    Optional<Submission> findByFormAndId(Form form, Long submissionId);
}
