package com.coolSchool.CoolSchool.controllers;

import com.coolSchool.CoolSchool.models.dto.common.QuizDTO;
import com.coolSchool.CoolSchool.models.dto.common.QuizDataDTO;
import com.coolSchool.CoolSchool.services.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quizzes")
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<QuizDTO>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDTO> getQuizById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(quizService.getQuizById(id));
    }

    @GetMapping("/subsection/{id}")
    public ResponseEntity<List<QuizDTO>> getQuizzesBySubsectionId(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(quizService.getQuizzesBySubsectionId(id));
    }

    @PostMapping("/create")
    public ResponseEntity<QuizDTO> createQuiz(@RequestBody QuizDataDTO quizDataDTO) {
        QuizDTO cratedQuiz = quizService.createQuiz(quizDataDTO);
        return new ResponseEntity<>(cratedQuiz, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizDTO> updateQuiz(@PathVariable("id") Long id, @Valid @RequestBody QuizDTO quizDTO) {
        return ResponseEntity.ok(quizService.updateQuiz(id, quizDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuizById(@PathVariable("id") Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.ok("Quiz with id: " + id + " has been deleted successfully!");
    }
}
