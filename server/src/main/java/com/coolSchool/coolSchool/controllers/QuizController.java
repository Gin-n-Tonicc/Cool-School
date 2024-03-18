package com.coolSchool.coolSchool.controllers;

import com.coolSchool.coolSchool.exceptions.answer.filters.JwtAuthenticationFilter;
import com.coolSchool.coolSchool.interfaces.RateLimited;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.common.*;
import com.coolSchool.coolSchool.models.dto.request.SaveUserProgressRequestDTO;
import com.coolSchool.coolSchool.services.QuizService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller class for handling operations related to quizzes.
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

    @GetMapping("/info/{id}")
    public ResponseEntity<QuizDTO> getQuizInfoById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(quizService.getQuizInfoById(id));
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

    @RateLimited
    @PostMapping("/create")
    public ResponseEntity<QuizDTO> createQuiz(@RequestBody QuizDataDTO quizDataDTO) {
        QuizDTO cratedQuiz = quizService.createQuiz(quizDataDTO);
        return new ResponseEntity<>(cratedQuiz, HttpStatus.CREATED);
    }

    @RateLimited
    @PutMapping("/{id}")
    public ResponseEntity<QuizDTO> updateQuiz(@PathVariable Long id, @RequestBody QuizDataDTO updatedQuizData) {
        QuizDTO updatedQuiz = quizService.updateQuiz(id, updatedQuizData);
        return ResponseEntity.ok(updatedQuiz);
    }

    @RateLimited
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuizById(@PathVariable("id") Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.ok("Quiz with id: " + id + " has been deleted successfully!");
    }

    @RateLimited
    @PostMapping("/{quizId}/take")
    public ResponseEntity<QuizAttemptDTO> takeQuiz(@PathVariable Long quizId, HttpServletRequest httpServletRequest) {
        PublicUserDTO publicUserDTO = (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey);
        QuizAttemptDTO quizAttemptDTO = quizService.takeQuiz(quizId, publicUserDTO.getId());
        quizService.deleteAutoSavedProgress(publicUserDTO.getId(), quizId);
        return ResponseEntity.ok(quizAttemptDTO);
    }

    @RateLimited
    @PutMapping("/{quizId}/submit/{attemptId}")
    public ResponseEntity<QuizResultDTO> submitQuiz(@PathVariable Long quizId, @PathVariable Long attemptId, @RequestBody List<UserAnswerDTO> userAnswers, HttpServletRequest httpServletRequest) {
        PublicUserDTO publicUserDTO = (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey);
        QuizResultDTO quizResultDTO = quizService.submitQuiz(quizId, userAnswers, publicUserDTO.getId(), attemptId);
        quizService.deleteAutoSavedProgress(publicUserDTO.getId(), quizId);
        return ResponseEntity.ok(quizResultDTO);
    }

    // Automatically saves user progress for a quiz.
    // In case the user unintentionally exits the test
    @PostMapping("/{quizId}/save-progress")
    public ResponseEntity<List<UserQuizProgressDTO>> autoSaveUserProgress(@PathVariable Long quizId, @RequestBody SaveUserProgressRequestDTO saveUserProgressRequestDTO, HttpServletRequest httpServletRequest) {
        PublicUserDTO publicUserDTO = (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey);
        List<UserQuizProgressDTO> userProgresses = quizService.autoSaveUserProgress(quizId, saveUserProgressRequestDTO.getQuestionId(), saveUserProgressRequestDTO.getAnswerId(), publicUserDTO.getId(), saveUserProgressRequestDTO.getAttemptId());
        return ResponseEntity.ok(userProgresses);
    }

    @GetMapping("/attempt/{quizAttemptId}") // Retrieves details of a quiz attempt.
    public ResponseEntity<QuizAttemptDTO> getQuizAttemptDetails(@PathVariable Long quizAttemptId) {
        QuizAttemptDTO quizAttemptDTO = quizService.getQuizAttemptDetails(quizAttemptId);
        return new ResponseEntity<>(quizAttemptDTO, HttpStatus.OK);
    }

    @GetMapping("/all/attempts/{quizId}") // Retrieves all attempts of a user in a quiz.
    public ResponseEntity<List<QuizAttemptDTO>> getAllUserAttemptsInAQuiz(@PathVariable Long quizId, HttpServletRequest httpServletRequest) {
        PublicUserDTO publicUserDTO = (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey);
        List<QuizAttemptDTO> quizAttemptsDTO = quizService.getAllUserAttemptsInAQuiz(quizId, publicUserDTO);
        return new ResponseEntity<>(quizAttemptsDTO, HttpStatus.OK);
    }

    @GetMapping("/highest-scores") // Retrieves the highest scores achieved by the current user in quizzes.
    public ResponseEntity<List<QuizAttemptDTO>> getUserHighestScoresInQuizzes(HttpServletRequest httpServletRequest) {
        PublicUserDTO publicUserDTO = (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey);
        List<QuizAttemptDTO> highestScores = quizService.getAllUserHighestScoresInQuizzes(publicUserDTO.getId());
        return ResponseEntity.ok().body(highestScores);
    }

    @GetMapping("/calculate-success-percentage")  // Calculates the success percentage of quizzes for the current user.
    public ResponseEntity<List<UserCourseDTO>> calculateQuizSuccessPercentageForCurrentUser(HttpServletRequest httpServletRequest) {
        PublicUserDTO publicUserDTO = (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey);
        List<UserCourseDTO> userCourseDTOs = quizService.calculateQuizSuccessPercentageForCurrentUser(publicUserDTO);
        return ResponseEntity.ok(userCourseDTOs);
    }
}
