package com.example.formscreator.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.formscreator.dto.FormDto;
import com.example.formscreator.entity.Form;
import com.example.formscreator.service.FormService;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/forms")
public class FormController {

    Logger logger = LoggerFactory.getLogger(FormController.class);

    @Autowired
    private FormService formService;
    
    @PostMapping("/")
    public Form saveForm(@Valid @RequestBody FormDto formDto){
        logger.info("Form Creation Request with Form Title: "+formDto.getTitle());
        return this.formService.saveForm(formDto);
    }

    @GetMapping("/")
    public List<Form> getAllForms(){
        logger.info("Form Get Request");
        return this.formService.getAllForms();
    }

    @GetMapping("/{id}")
    public Form getFormById(@PathVariable Long id){
        logger.info("Form Get Request with Id: "+id);
        return this.formService.getFormById(id);
    }

    @PutMapping("/{id}")
    public Form updateFormById(@PathVariable Long id, @Valid @RequestBody FormDto formDto) {
        logger.info("Form Update Request with Id: "+id);
        return this.formService.updateFormById(id, formDto);
    }

    @DeleteMapping("/{id}")
    public String deleteFormById(@PathVariable Long id){
        logger.info("Form Delete Request with Id: "+id);
        return this.formService.deleteFormById(id);
    }

}
