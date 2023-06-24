package com.example.formscreator.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.formscreator.dto.FormDto;
import com.example.formscreator.entity.Form;
import com.example.formscreator.entity.Question;
import com.example.formscreator.entity.Submission;
import com.example.formscreator.entity.User;
import com.example.formscreator.exception.ResourceNotFoundException;
import com.example.formscreator.repository.FormRepository;

import javax.transaction.Transactional;

@Transactional
@Service
public class FormService {
    
    @Autowired
    private FormRepository formRepository;

    @Autowired
    private AuthenticationService authenticationService;

    // helper functions
    private Form copyFormFromDto(Form form, FormDto formDto){
        form.setTitle(formDto.getTitle());
        form.setDescription(formDto.getDescription());
        form.setEnabled(formDto.isEnabled());
        return form;
    }

    public User checkFormOwner(Long formId){
        Form existingForm = getFormById(formId);
        return checkFormOwner(existingForm);
    }

    public User checkFormOwner(Form form){
        User user = authenticationService.getUser();
        if(!form.getUser().equals(user)){
            throw new AccessDeniedException("Access To Form is forbidden");
        }
        return user;
    }

    // create form
    public Form saveForm(FormDto formDto){
        Form form = copyFormFromDto(new Form(),formDto);
        form.setUser(authenticationService.getUser());
        form.setSubmissions(new ArrayList<Submission>());
        form.setQuestions(new ArrayList<Question>());
        return this.formRepository.save(form);
    }

    // get all forms
    public List<Form> getAllForms(){
        // return this.formRepository.findByUser(authenticationService.getUser());
        return this.formRepository.findByUserId(authenticationService.getUser().getId());
    }

    // get specific form
    public Form getFormById(Long formId){
        return this.formRepository.findById(formId).orElseThrow(()-> new ResourceNotFoundException("Form not found"));
    }

    // update form
    public Form updateFormById(Long formId, FormDto formDto){
        Form form = getFormById(formId);
        checkFormOwner(form);
        form = copyFormFromDto(form, formDto);
        return this.formRepository.save(form);
    }

    // delete form
    public  String deleteFormById(Long formId){
        checkFormOwner(formId);
        this.formRepository.deleteById(formId);
        return "Form Deletion Success";
    }
}
