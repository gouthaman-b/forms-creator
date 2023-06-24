package com.example.formscreator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.formscreator.dto.QuestionDto;
import com.example.formscreator.entity.Form;
import com.example.formscreator.entity.Option;
import com.example.formscreator.entity.Question;
import com.example.formscreator.exception.InvalidRequestException;
import com.example.formscreator.exception.ResourceNotFoundException;
import com.example.formscreator.repository.QuestionRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class QuestionService {
    
    @Autowired
    private QuestionRepository questionRepository;


    @Autowired
    private FormService formService;

    private List<String> questionTypes = List.of("text","number","date","time","range","tel","url","color","email","tel","url","radio","checkbox","select");

    private List<String> questionTypesWithMinMax = questionTypes.subList(1, 5);

    private List<String> questionTypesWithOptions = questionTypes.subList(11,14);

    // helper functions
    private Question copyQuestionFromDto(Question question, QuestionDto questionDto){
        question.setTitle(questionDto.getTitle());
        question.setDescription(questionDto.getDescription());
        if(!questionTypes.contains (questionDto.getType())){
            throw new InvalidRequestException("Question Type Invalid");
        }
        question.setType(questionDto.getType());
        question.setRequired(questionDto.isRequired());
        if(questionTypesWithMinMax.contains(question.getType())){
            question.setMin(questionDto.getMin());
            question.setMax(questionDto.getMax());
        } else{
            question.setMin(null);
            question.setMax(null);
        }
        return question;
    }

    private Option createOption(long orderNumber, String text, Question question){
        Option option = new Option();
        option.setText(text);
        option.setQuestion(question);
        return option;
    }

    private Long getMaxOrderNumber(List<Question> questions){
        Optional<Long> maxOrderNumber = questions.stream()
        .map(Question::getOrderNumber)
        .max(Long::compareTo);
    
        return maxOrderNumber.orElse(0L);
    }

    private void adjustOrderNumbers(Form form, Long fromOrder, Long toOrder) {
        List<Question> questions = form.getQuestions();
        for (Question question : questions) {
            Long orderNumber = question.getOrderNumber();
            if(toOrder==-1L){
                if(orderNumber>fromOrder){
                    question.setOrderNumber(orderNumber-1);
                }
            }else{
                if(fromOrder>toOrder){
                    if(orderNumber>=toOrder && orderNumber<fromOrder){
                        question.setOrderNumber(orderNumber+1);
                    }
                }else if(fromOrder<toOrder){
                    if(orderNumber>fromOrder && orderNumber<=toOrder){
                        question.setOrderNumber(orderNumber-1);
                    }
                }
            }
        }
        questionRepository.saveAll(questions);
    }



    // get all questions in a form
    public List<Question> getQuestionsByFormId(Long formId){
        
        Form form = this.formService.getFormById(formId);
        
        List<Question> questions = form.getQuestions();
        return questions;
    }

    // get a question in a form by id
    public Question getQuestionById(Long formId, Long questionId){

        List<Question> questions = getQuestionsByFormId(formId);
        
        for (Question question : questions) {
            if (question.getId() == questionId) {
                return question;
            }
        }
        throw new ResourceNotFoundException("Question not found");
    }

    // create a question in a form if user is owner
    public Question saveQuestion(Long formId, QuestionDto questionDto){
        
        Form form = this.formService.getFormById(formId);
        this.formService.checkFormOwner(form);
        
        Question question = copyQuestionFromDto(new Question(), questionDto);
        
        if(questionTypesWithOptions.contains(question.getType())){
            List<Option> options = new ArrayList<Option>();
            List<String> userOptions = questionDto.getOptions();
            if(userOptions!=null){
                for(int i = 0; i < userOptions.size(); i++){
                    options.add(createOption(i, userOptions.get(i), question));
                }
                question.setOptions(options);
            }
        }
        
        if(form.getQuestions().size()>0){
            question.setOrderNumber(getMaxOrderNumber(form.getQuestions())+1);
        }else{
            question.setOrderNumber(1L);
        }
        
        question.setForm(form);
        List<Question> questions = form.getQuestions();
        questions.add(question);
        form.setQuestions(questions);

        return this.questionRepository.save(question);
    }

    // update a question in a form if user is owner
    public Question updateQuestionById(Long formId, Long questionId, QuestionDto questionDto){
        
        this.formService.checkFormOwner(formId);
        
        Question question = getQuestionById(formId, questionId);
        question = copyQuestionFromDto(question, questionDto);
        
        question.getOptions().clear();
        if(questionTypesWithOptions.contains(question.getType())){
            List<String> userOptions = questionDto.getOptions();
            if(userOptions!=null){
                List<Option> options = new ArrayList<Option>();       
                for(int i = 0; i < userOptions.size(); i++){
                    options.add(createOption(i, userOptions.get(i), question));
                }
                question.getOptions().addAll(options);
            }
        }

        if (questionDto.getOrderNumber() != question.getOrderNumber()) {
            adjustOrderNumbers(question.getForm(), question.getOrderNumber(), questionDto.getOrderNumber());
            question.setOrderNumber(questionDto.getOrderNumber());
        }
        
        return this.questionRepository.save(question);
    }

    // delete a question in a form if user is owner
    public String deleteQuestionById(Long formId, Long questionId){
        
        this.formService.checkFormOwner(formId);
        Question question = getQuestionById(formId, questionId);

        adjustOrderNumbers(question.getForm(), question.getOrderNumber(), -1L);
        question.getResponses().clear();

        this.questionRepository.deleteById(questionId);
        return "Deletion Success";
    }
}
