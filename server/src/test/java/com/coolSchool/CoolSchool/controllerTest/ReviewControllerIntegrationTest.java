package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.ReviewController;
import com.coolSchool.coolSchool.exceptions.answer.filters.JwtAuthenticationFilter;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.request.ReviewRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.ReviewResponseDTO;
import com.coolSchool.coolSchool.services.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewControllerIntegrationTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @Test
    void testGetReviewByCourseId() {
        Long courseId = 1L;
        List<ReviewResponseDTO> reviews = new ArrayList<>();
        reviews.add(new ReviewResponseDTO());
        reviews.add(new ReviewResponseDTO());

        reviews.get(0).setId(1L);
        reviews.get(0).setText("Review 1");
        reviews.get(1).setId(2L);
        reviews.get(1).setText("Review 2");

        when(reviewService.getAllReviews(courseId)).thenReturn(reviews);

        ResponseEntity<List<ReviewResponseDTO>> responseEntity = reviewController.getReviewByCourseId(courseId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(reviews, responseEntity.getBody());
    }

    @Test
    void testGetReviewById() {
        Long reviewId = 1L;
        ReviewResponseDTO review = new ReviewResponseDTO();

        review.setId(1L);
        review.setText("Review 1");

        when(reviewService.getReviewById(reviewId)).thenReturn(review);

        ResponseEntity<ReviewResponseDTO> responseEntity = reviewController.getReviewById(reviewId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(review, responseEntity.getBody());
    }

    @Test
    void testCreateReview() {
        ReviewRequestDTO reviewDTO = new ReviewRequestDTO();
        ReviewResponseDTO createdReview = new ReviewResponseDTO();

        createdReview.setId(1L);
        createdReview.setText("Created Review");

        HttpServletRequest request = mock(HttpServletRequest.class);
        PublicUserDTO userDTO = new PublicUserDTO();
        when(request.getAttribute(JwtAuthenticationFilter.userKey)).thenReturn(userDTO);

        when(reviewService.createReview(reviewDTO, userDTO)).thenReturn(createdReview);

        ResponseEntity<ReviewResponseDTO> responseEntity = reviewController.createReview(reviewDTO, request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(createdReview, responseEntity.getBody());
    }

    @Test
    void testDeleteReviewById() {
        Long reviewId = 1L;
        HttpServletRequest request = mock(HttpServletRequest.class);
        PublicUserDTO userDTO = new PublicUserDTO();
        when(request.getAttribute(JwtAuthenticationFilter.userKey)).thenReturn(userDTO);

        ResponseEntity<String> responseEntity = reviewController.deleteReviewById(reviewId, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Review with id: 1 has been deleted successfully!", responseEntity.getBody());
    }
}