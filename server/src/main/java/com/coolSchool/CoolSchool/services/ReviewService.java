package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.CoolSchool.models.dto.request.ReviewRequestDTO;
import com.coolSchool.CoolSchool.models.dto.response.ReviewResponseDTO;

import java.util.List;

public interface ReviewService {
    List<ReviewResponseDTO> getAllReviews(Long courseId);

    ReviewResponseDTO getReviewById(Long id);

    ReviewResponseDTO createReview(ReviewRequestDTO reviewDTO, PublicUserDTO loggedUser);

    void deleteReview(Long id, PublicUserDTO loggedUser);
}
