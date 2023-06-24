package com.example.formscreator.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.formscreator.dto.ResponseDto;
import com.example.formscreator.dto.SubmissionDto;
import com.example.formscreator.entity.Form;
import com.example.formscreator.entity.Option;
import com.example.formscreator.entity.Question;
import com.example.formscreator.entity.Response;
import com.example.formscreator.entity.Submission;
import com.example.formscreator.entity.User;
import com.example.formscreator.exception.InvalidRequestException;
import com.example.formscreator.exception.ResourceNotFoundException;
import com.example.formscreator.repository.SubmissionRepository;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private FormService formService;

    // helper functions
    private Response createResponse(String answer, Submission submission, Question question){
        Response response = new Response();
        if(question.isRequired() && answer=="") throw new InvalidRequestException("Required field cannot be empty");
        response.setAnswer(answer);
        response.setSubmission(submission);
        response.setQuestion(question);
        return response;
    }

    private SubmissionDto createSubmissionDto(Submission submission){
        SubmissionDto sub = new SubmissionDto();
        sub.setId(submission.getId());
        sub.setFormId(submission.getForm().getId());
        sub.setCreatedAt(submission.getCreatedAt());
        sub.setUserEmail(submission.getUser().getEmail());
        List<ResponseDto> responses = new ArrayList<ResponseDto>();
        for(Response res:submission.getResponses()){
            ResponseDto responseDto = new ResponseDto();
            responseDto.setId(res.getId());
            responseDto.setQuestionId(res.getQuestion().getId());
            responseDto.setAnswer(res.getAnswer());
            responses.add(responseDto);
        }
        sub.setResponses(responses);
        return sub;
    }

    private void checkPreviousSubmission(Form form, User user){
        List<Submission> submissions = form.getSubmissions();
        
        for (Submission submission : submissions) {
            User prevUser = submission.getUser();
            if (prevUser.getId().equals(user.getId())) {
                throw  new InvalidRequestException("Form Already Submitted");
            }
        }
    }

    private void validateSubmission(List<Response> responses,List<Question> questions) {
        // Validate count of responses
        if (responses.size() != questions.size()) {
            throw new InvalidRequestException("Number of responses does not match the number of questions.");
        }
    
        Set<Long> questionIds = new HashSet<>();
        for (Response response : responses) {
            // Validate uniqueness of questionId
            Question question = response.getQuestion();

            if (!questionIds.add(question.getId())) {
                throw new InvalidRequestException("Duplicate questionId found in responses.");
            }
            
            // Validate the answer based on the question type
            if (question.getOptions()!=null && question.getOptions().size()>0) {
                List<String> optionTexts = question.getOptions().stream()
                        .map(Option::getText)
                        .collect(Collectors.toList());
                
                List<String> answers = List.of(response.getAnswer());
                if(question.getType().equals("checkbox")){
                    answers = List.of(response.getAnswer().split("!>"));
                }
                for (String answer : answers) {
                    if (!optionTexts.contains(answer)) {
                        throw new InvalidRequestException("Invalid answer for multiple-choice question.");
                    }
                }
            }
        }
    }
    
    
    // create submission for a form with user
    public SubmissionDto saveSubmission(Long formId, List<ResponseDto> responseList){
        // check form exist and get form
        Form form = this.formService.getFormById(formId);
        if(!form.isEnabled()) throw new AccessDeniedException("Form is not opened for submissions");
        // get user id
        User user = this.authenticationService.getUser();
        checkPreviousSubmission(form, user);

        List<Question> questions = form.getQuestions();

        // create response obj
        Submission submission = new Submission();
        submission.setForm(form);
        submission.setUser(user);
        List<Response> responses = new ArrayList<Response>();
        

        for(ResponseDto responseDto: responseList){
            Question question = questions
                    .stream()
                    .filter(q -> q.getId().equals(responseDto.getQuestionId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
            Response response = createResponse(responseDto.getAnswer(), submission, question);
            
            responses.add(response);
        }
        validateSubmission(responses, questions);
        
        submission.setResponses(responses);
        this.submissionRepository.save(submission);

        SubmissionDto sub = createSubmissionDto(submission);
        return sub;
    }

    // get all the submissions of form and check if user is owner
    public List<SubmissionDto> getSubmissionsByFormId(Long formId){
        // check form owner
        this.formService.checkFormOwner(formId);
        Form form = this.formService.getFormById(formId);
        // return submissions by form id
        List<Submission> submissions = this.submissionRepository.findByForm(form);
        List<SubmissionDto> sub = new ArrayList<SubmissionDto>();
        for(Submission submission : submissions){
            sub.add(createSubmissionDto(submission));
        }
        return sub;
    }

    // get specific submission of form and check if user is owner
    public SubmissionDto getSubmissionById(Long formId, Long submissionId){
        // get all submissions filter by id
        this.formService.checkFormOwner(formId);
        Form form = this.formService.getFormById(formId);
        Submission submission = this.submissionRepository.findByFormAndId(form,submissionId).orElseThrow(()->new ResourceNotFoundException("Cannot find submission"));
        return createSubmissionDto(submission);
    }

    public String deleteSubmissionById(Long formId, Long submissionId){
        this.formService.checkFormOwner(formId);
        Form form = this.formService.getFormById(formId);
        Submission submission = this.submissionRepository.findByFormAndId(form,submissionId).orElseThrow(()->new ResourceNotFoundException("Cannot find submission"));
        this.submissionRepository.delete(submission);
        return "Deletion Success";
    }
}
