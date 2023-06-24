package com.example.formscreator.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.formscreator.dto.QuestionDto;
import com.example.formscreator.entity.Question;
import com.example.formscreator.service.QuestionService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/forms/{formId}/questions")
public class QuestionController {

    Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public List<Question> getAllQuestions(@PathVariable Long formId){
        logger.info("Question Get Request with Form Id: "+formId);
        return this.questionService.getQuestionsByFormId(formId);
    }

    @PostMapping("/")
    public Question saveQuestion(@PathVariable Long formId, @Valid @RequestBody QuestionDto questionDto){
        logger.info("Question Creation Request with Form Id: "+formId+" and Question title: "+questionDto.getTitle());
        return this.questionService.saveQuestion(formId, questionDto);
    }

    @GetMapping("/{questionId}")
    public Question getQuestionById(@PathVariable Long formId, @PathVariable Long questionId){
        logger.info("Question Get Request with Form Id: "+formId+" and Question Id: "+questionId);
        return this.questionService.getQuestionById(formId, questionId);
    }

    @PutMapping("/{questionId}")
    public Question updateQuestionById(@PathVariable Long formId, @PathVariable Long questionId, @Valid @RequestBody QuestionDto questionDto){
        logger.info("Question Update Request with Form Id: "+formId+" and Question Id: "+questionId);
        return this.questionService.updateQuestionById(formId, questionId, questionDto);
    }

    @DeleteMapping("/{questionId}")
    public String deleteQuestionById(@PathVariable Long formId, @PathVariable Long questionId){
        logger.info("Question Delete Request with Form Id: "+formId+" and Question Id: "+questionId);
        return this.questionService.deleteQuestionById(formId, questionId);
    }
    
}
