package com.coolSchool.coolSchool.controllers;

import com.coolSchool.coolSchool.interfaces.RateLimited;
import com.coolSchool.coolSchool.models.dto.common.QuestionDTO;
import com.coolSchool.coolSchool.services.QuestionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling operations related to questions.
 * CRUD
 */
@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(questionService.getQuestionById(id));
    }

    @RateLimited
    @PostMapping("/create")
    public ResponseEntity<QuestionDTO> createQuestion(@Valid @RequestBody QuestionDTO questionDTO) {
        QuestionDTO cratedQuestion = questionService.createQuestion(questionDTO);
        return new ResponseEntity<>(cratedQuestion, HttpStatus.CREATED);
    }

    @RateLimited
    @PutMapping("/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable("id") Long id, @Valid @RequestBody QuestionDTO questionDTO) {
        return ResponseEntity.ok(questionService.updateQuestion(id, questionDTO));
    }

    @RateLimited
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuestionById(@PathVariable("id") Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok("Question with id: " + id + " has been deleted successfully!");
    }
}
