package com.example.formscreator.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.formscreator.dto.ResponseDto;
import com.example.formscreator.dto.SubmissionDto;
import com.example.formscreator.service.SubmissionService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/forms/{formId}/submissions")
public class SubmissionController {

    Logger logger = LoggerFactory.getLogger(QuestionController.class);
    
    @Autowired
    private SubmissionService submissionService;

    @GetMapping("/")
    public List<SubmissionDto> getSubmissionsByFormId(@PathVariable Long formId){
        logger.info("Submission Get Request with Form Id: "+formId);
        return this.submissionService.getSubmissionsByFormId(formId);
    }

    @PostMapping("/")
    public SubmissionDto saveSubmission(@PathVariable Long formId, @Valid @RequestBody List<ResponseDto> Responses){
        logger.info("Submission Creation Request with Form Id: "+formId);
        return this.submissionService.saveSubmission(formId, Responses);
    }

    @GetMapping("/{submissionId}")
    public SubmissionDto getSubmissionById(@PathVariable Long formId, @PathVariable Long submissionId){
        logger.info("Submission Get Request with Form Id: "+formId+" and Submission Id: "+submissionId);
        return this.submissionService.getSubmissionById(formId, submissionId);
    }

    @DeleteMapping("/{submissionId}")
    public String deleteSubmissionById(@PathVariable Long formId, @PathVariable Long submissionId){
        logger.info("Submission Delete Request with Form Id: "+formId+" and Submission Id: "+submissionId);
        return this.submissionService.deleteSubmissionById(formId, submissionId);
    }


}
