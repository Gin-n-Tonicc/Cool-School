package com.coolSchool.coolSchool.controllers;

import com.coolSchool.coolSchool.exceptions.answer.filters.JwtAuthenticationFilter;
import com.coolSchool.coolSchool.interfaces.RateLimited;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.request.ReviewRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.ReviewResponseDTO;
import com.coolSchool.coolSchool.services.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A controller class for handling resource-related operations.
 * All users enrolled in a course can review it with starts and a message.
 */
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/all/{courseId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewByCourseId(@PathVariable(name = "courseId") Long id) {
        return ResponseEntity.ok(reviewService.getAllReviews(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @RateLimited
    @PostMapping("/create")
    public ResponseEntity<ReviewResponseDTO> createReview(@Valid @RequestBody ReviewRequestDTO reviewDTO, HttpServletRequest httpServletRequest) {
        ReviewResponseDTO createdReview = reviewService.createReview(reviewDTO, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey));
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @RateLimited
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReviewById(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        reviewService.deleteReview(id, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey));
        return ResponseEntity.ok("Review with id: " + id + " has been deleted successfully!");
    }
}
