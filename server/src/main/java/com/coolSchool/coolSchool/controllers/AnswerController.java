package com.coolSchool.coolSchool.controllers;

import com.coolSchool.coolSchool.interfaces.RateLimited;
import com.coolSchool.coolSchool.models.dto.common.AnswerDTO;
import com.coolSchool.coolSchool.services.AnswerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A controller class for handling answer-related operations.
 * CRUD
 */
@RestController
@RequestMapping("/api/v1/answers")
public class AnswerController {
    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<AnswerDTO>> getAllAnswers() {
        return ResponseEntity.ok(answerService.getAllAnswers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnswerDTO> getAnswerById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(answerService.getAnswerById(id));
    }

    @RateLimited
    @PostMapping("/create")
    public ResponseEntity<AnswerDTO> createAnswer(@Valid @RequestBody AnswerDTO answerDTO) {
        AnswerDTO cratedAnswer = answerService.createAnswer(answerDTO);
        return new ResponseEntity<>(cratedAnswer, HttpStatus.CREATED);
    }

    @RateLimited
    @PutMapping("/{id}")
    public ResponseEntity<AnswerDTO> updateAnswer(@PathVariable("id") Long id, @Valid @RequestBody AnswerDTO answerDTO) {
        return ResponseEntity.ok(answerService.updateAnswer(id, answerDTO));
    }

    @RateLimited
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnswerById(@PathVariable("id") Long id) {
        answerService.deleteAnswer(id);
        return ResponseEntity.ok("Answer with id: " + id + " has been deleted successfully!");
    }
}
