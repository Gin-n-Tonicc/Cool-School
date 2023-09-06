package com.coolSchool.CoolSchool.controllers;

import com.coolSchool.CoolSchool.models.dto.UserAnswerDTO;
import com.coolSchool.CoolSchool.services.impl.UserAnswerServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/userAnswers")
public class UserAnswerController {
    private final UserAnswerServiceImpl userAnswerService;

    public UserAnswerController(UserAnswerServiceImpl userAnswerService) {
        this.userAnswerService = userAnswerService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserAnswerDTO>> getAllUserAnswers() {
        return ResponseEntity.ok(userAnswerService.getAllUserAnswers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAnswerDTO> getUserAnswerById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(userAnswerService.getUserAnswerById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<UserAnswerDTO> createUserAnswer(@Valid @RequestBody UserAnswerDTO userAnswerDTO) {
        userAnswerDTO.setAttemptNumber(userAnswerService.calculateTheNextAttemptNumber(userAnswerDTO.getUserId(), userAnswerDTO.getAnswerId()));
        UserAnswerDTO cratedUserAnswer = userAnswerService.createUserAnswer(userAnswerDTO);
        return new ResponseEntity<>(cratedUserAnswer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAnswerDTO> updateUserAnswer(@PathVariable("id") Long id, @Valid @RequestBody UserAnswerDTO userAnswerDTO) {
        return ResponseEntity.ok(userAnswerService.updateUserAnswer(id, userAnswerDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserAnswerById(@PathVariable("id") Long id) {
        userAnswerService.deleteUserAnswer(id);
        return ResponseEntity.ok("UserAnswer with id: " + id + " has been deleted successfully!");
    }
}

