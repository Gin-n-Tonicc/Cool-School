package com.coolSchool.CoolSchool.controllers;

import com.coolSchool.CoolSchool.models.dto.UserAnswerDTO;
import com.coolSchool.CoolSchool.services.impl.UserAnswerServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/userUserAnswers")
public class UserAnswerController {
    private final UserAnswerServiceImpl userUserAnswerService;

    public UserAnswerController(UserAnswerServiceImpl userUserAnswerService) {
        this.userUserAnswerService = userUserAnswerService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserAnswerDTO>> getAllUserAnswers() {
        return ResponseEntity.ok(userUserAnswerService.getAllUserAnswers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAnswerDTO> getUserAnswerById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(userUserAnswerService.getUserAnswerById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<UserAnswerDTO> createUserAnswer(@Valid @RequestBody UserAnswerDTO userUserAnswerDTO) {
        UserAnswerDTO cratedUserAnswer = userUserAnswerService.createUserAnswer(userUserAnswerDTO);
        return new ResponseEntity<>(cratedUserAnswer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAnswerDTO> updateUserAnswer(@PathVariable("id") Long id, @Valid @RequestBody UserAnswerDTO userUserAnswerDTO) {
        return ResponseEntity.ok(userUserAnswerService.updateUserAnswer(id, userUserAnswerDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserAnswerById(@PathVariable("id") Long id) {
        userUserAnswerService.deleteUserAnswer(id);
        return ResponseEntity.ok("UserAnswer with id: " + id + " has been deleted successfully!");
    }
}

