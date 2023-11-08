package com.coolSchool.CoolSchool.services.impl;

import com.coolSchool.CoolSchool.enums.Role;
import com.coolSchool.CoolSchool.exceptions.common.AccessDeniedException;
import com.coolSchool.CoolSchool.exceptions.common.NoSuchElementException;
import com.coolSchool.CoolSchool.exceptions.course.ValidationCourseException;
import com.coolSchool.CoolSchool.exceptions.review.ReviewNotFoundException;
import com.coolSchool.CoolSchool.models.dto.CourseDTO;
import com.coolSchool.CoolSchool.models.dto.ReviewDTO;
import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.CoolSchool.models.entity.Course;
import com.coolSchool.CoolSchool.models.entity.Review;
import com.coolSchool.CoolSchool.repositories.CourseRepository;
import com.coolSchool.CoolSchool.repositories.ReviewRepository;
import com.coolSchool.CoolSchool.repositories.UserRepository;
import com.coolSchool.CoolSchool.services.CourseService;
import com.coolSchool.CoolSchool.services.ReviewService;
import jakarta.validation.ConstraintViolationException;
import org.modelmapper.ModelMapper;
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

    public ReviewServiceImpl(ReviewRepository reviewRepository, CourseService courseService, UserRepository userRepository, CourseRepository courseRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.courseService = courseService;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ReviewDTO> getAllReviews(Long courseId) {
        CourseDTO courseDTO = courseService.getCourseById(courseId);
        List<Review> reviews = reviewRepository.findAllByCourse(modelMapper.map(courseDTO, Course.class));
        return reviews.stream().map(review -> modelMapper.map(review, ReviewDTO.class)).toList();
    }

    @Override
    public ReviewDTO getReviewById(Long id) {
        Optional<Review> review = reviewRepository.findByIdAndDeletedFalse(id);
        if (review.isPresent()) {
            return modelMapper.map(review.get(), ReviewDTO.class);
        }
        throw new ReviewNotFoundException();
    }

    @Override
    public ReviewDTO createReview(ReviewDTO reviewDTO, PublicUserDTO loggedUser) {
        if (loggedUser == null) {
            throw new AccessDeniedException();
        }
        try {
            reviewDTO.setId(null);
            userRepository.findByIdAndDeletedFalse(reviewDTO.getUser().getId()).orElseThrow(NoSuchElementException::new);
            Course course = courseRepository.findByIdAndDeletedFalse(reviewDTO.getCourse().getId()).orElseThrow(NoSuchElementException::new);
            Review review = reviewRepository.save(modelMapper.map(reviewDTO, Review.class));
            updateCourseStars(course);
            return modelMapper.map(review, ReviewDTO.class);
        } catch (ConstraintViolationException exception) {
            throw new ValidationCourseException(exception.getConstraintViolations());
        }
    }

    @Override
    public void deleteReview(Long id, PublicUserDTO loggedUser) {
        Optional<Review> review = reviewRepository.findByIdAndDeletedFalse(id);
        if (review.isPresent()) {
            if (loggedUser == null || !(Objects.equals(loggedUser.getId(), review.get().getUser().getId()) && !(loggedUser.getRole().equals(Role.ADMIN)))) {
                throw new AccessDeniedException();
            }
            review.get().setDeleted(true);
            reviewRepository.save(review.get());
            updateCourseStars(review.get().getCourse());
        } else {
            throw new ReviewNotFoundException();
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
