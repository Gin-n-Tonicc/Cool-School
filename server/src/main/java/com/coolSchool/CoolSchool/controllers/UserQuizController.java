package com.coolSchool.CoolSchool.controllers;

import com.coolSchool.CoolSchool.models.dto.UserQuizDTO;
import com.coolSchool.CoolSchool.services.impl.UserQuizServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/userQuizzes")
public class UserQuizController {
    private final UserQuizServiceImpl userQuizService;

    public UserQuizController(UserQuizServiceImpl userQuizService) {
        this.userQuizService = userQuizService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserQuizDTO>> getAllUserQuizzes() {
        return ResponseEntity.ok(userQuizService.getAllUserQuizzes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserQuizDTO> getUserQuizById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(userQuizService.getUserQuizById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<UserQuizDTO> createUserQuiz(@Valid @RequestBody UserQuizDTO userQuizDTO) {
        UserQuizDTO cratedUserQuiz = userQuizService.createUserQuiz(userQuizDTO);
        return new ResponseEntity<>(cratedUserQuiz, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserQuizDTO> updateUserQuiz(@PathVariable("id") Long id, @Valid @RequestBody UserQuizDTO userQuizDTO) {
        return ResponseEntity.ok(userQuizService.updateUserQuiz(id, userQuizDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserQuizById(@PathVariable("id") Long id) {
        userQuizService.deleteUserQuiz(id);
        return ResponseEntity.ok("UserQuiz with id: " + id + " has been deleted successfully!");
    }

    @GetMapping("/calculateTotalMarks")
    public ResponseEntity<List<UserQuizDTO>> calculateUserTotalMarks(@RequestParam Long userId, @RequestParam Long quizId) {
        return ResponseEntity.ok(userQuizService.calculateUserTotalMarks(userId, quizId));
    }
}

