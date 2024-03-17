package com.coolSchool.coolSchool.services.impl;

import com.coolSchool.coolSchool.enums.Role;
import com.coolSchool.coolSchool.exceptions.common.AccessDeniedException;
import com.coolSchool.coolSchool.exceptions.course.CourseNotFoundException;
import com.coolSchool.coolSchool.exceptions.review.ReviewNotFoundException;
import com.coolSchool.coolSchool.exceptions.user.UserNotFoundException;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.request.ReviewRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.CourseResponseDTO;
import com.coolSchool.coolSchool.models.dto.response.ReviewResponseDTO;
import com.coolSchool.coolSchool.models.entity.Course;
import com.coolSchool.coolSchool.models.entity.Review;
import com.coolSchool.coolSchool.repositories.CourseRepository;
import com.coolSchool.coolSchool.repositories.ReviewRepository;
import com.coolSchool.coolSchool.repositories.UserRepository;
import com.coolSchool.coolSchool.services.CourseService;
import com.coolSchool.coolSchool.services.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final CourseService courseService;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    public ReviewServiceImpl(ReviewRepository reviewRepository, CourseService courseService, UserRepository userRepository, CourseRepository courseRepository, ModelMapper modelMapper, MessageSource messageSource) {
        this.reviewRepository = reviewRepository;
        this.courseService = courseService;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
    }

    @Override
    public List<ReviewResponseDTO> getAllReviews(Long courseId) {
        CourseResponseDTO courseDTO = courseService.getCourseById(courseId);
        List<Review> reviews = reviewRepository.findAllByCourse(modelMapper.map(courseDTO, Course.class));
        return reviews.stream().map(review -> modelMapper.map(review, ReviewResponseDTO.class)).toList();
    }

    @Override
    public ReviewResponseDTO getReviewById(Long id) {
        Optional<Review> review = reviewRepository.findByIdAndDeletedFalse(id);
        if (review.isPresent()) {
            return modelMapper.map(review.get(), ReviewResponseDTO.class);
        }
        throw new ReviewNotFoundException(messageSource);
    }

    @Override
    public ReviewResponseDTO createReview(ReviewRequestDTO reviewDTO, PublicUserDTO loggedUser) {
        if (loggedUser == null) {
            throw new AccessDeniedException(messageSource);
        }
        reviewDTO.setId(null);
        userRepository.findByIdAndDeletedFalse(reviewDTO.getUserId()).orElseThrow(() -> new UserNotFoundException(messageSource));
        Course course = courseRepository.findByIdAndDeletedFalse(reviewDTO.getCourseId()).orElseThrow(() -> new CourseNotFoundException(messageSource));
        Review reviewRequestDTO = modelMapper.map(reviewDTO, Review.class);
        Review review = reviewRepository.save(reviewRequestDTO);

        // After creating a new review - update the stars of the course
        updateCourseStars(course);
        return modelMapper.map(review, ReviewResponseDTO.class);
    }

    /**
     * Deletes a review.
     *
     * @param id         The ID of the review to be deleted.
     * @param loggedUser The DTO representing the logged-in user.
     * @throws ReviewNotFoundException If the review with the specified ID is not found.
     * @throws AccessDeniedException   If the logged-in user is not authorized or does not have the permissions to delete the review.
     */
    @Override
    public void deleteReview(Long id, PublicUserDTO loggedUser) {
        Optional<Review> review = reviewRepository.findByIdAndDeletedFalse(id);
        if (review.isPresent()) {
            if (loggedUser == null || !(Objects.equals(loggedUser.getId(), review.get().getUser().getId()) && !(loggedUser.getRole().equals(Role.ADMIN)))) {
                throw new AccessDeniedException(messageSource);
            }
            // Soft delete
            review.get().setDeleted(true);
            reviewRepository.save(review.get());
            // After deleting the review - update the course stars
            updateCourseStars(review.get().getCourse());
        } else {
            throw new ReviewNotFoundException(messageSource);
        }
    }

    private void updateCourseStars(Course course) {
        List<Review> reviews = reviewRepository.findAllByCourse(course);
        if (reviews.isEmpty()) {
            course.setStars(0);
        } else {
            double totalStars = 0;
            for (Review review : reviews) {
                totalStars += review.getStars();
            }
            double averageStars = totalStars / reviews.size();
            course.setStars(averageStars);
        }
        courseRepository.save(course);
    }
}
