package com.coolSchool.coolSchool.controllers;

import com.coolSchool.coolSchool.filters.JwtAuthenticationFilter;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.common.*;
import com.coolSchool.coolSchool.services.QuizService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<QuizQuestionsAnswersDTO> getQuizById(@PathVariable(name = "id") Long id, HttpServletRequest httpServletRequest) {
        PublicUserDTO publicUserDTO = (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey);
        return ResponseEntity.ok(quizService.getQuizById(id, publicUserDTO.getId()));
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

    @PostMapping("/{quizId}/take")
    public ResponseEntity<QuizResultDTO> takeQuiz(@PathVariable Long quizId, @RequestBody List<UserAnswerDTO> userAnswers, HttpServletRequest httpServletRequest) {
        PublicUserDTO publicUserDTO = (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey);
        QuizResultDTO quizResultDTO = quizService.takeQuiz(quizId, userAnswers, publicUserDTO.getId());
        quizService.deleteAutoSavedProgress(publicUserDTO.getId(), quizId);
        return ResponseEntity.ok(quizResultDTO);
    }

    @PostMapping("/quiz/{quizId}/save-progress")
    public ResponseEntity<String> autoSaveUserProgress(@PathVariable Long quizId, @RequestParam Long questionId, @RequestParam Long answerId, HttpServletRequest httpServletRequest) {
        PublicUserDTO publicUserDTO = (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey);
        quizService.autoSaveUserProgress(quizId, questionId, answerId, publicUserDTO.getId());
        return ResponseEntity.ok("Saved answer!");
    }
}
