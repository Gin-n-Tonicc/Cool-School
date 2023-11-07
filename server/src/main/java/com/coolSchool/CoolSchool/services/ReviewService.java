package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.ReviewDTO;
import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;

import java.util.List;

public interface ReviewService {
    List<ReviewDTO> getAllReviews(Long courseId);

    ReviewDTO getReviewById(Long id);

    ReviewDTO createReview(ReviewDTO reviewDTO, PublicUserDTO loggedUser);

    void deleteReview(Long id, PublicUserDTO loggedUser);
}
