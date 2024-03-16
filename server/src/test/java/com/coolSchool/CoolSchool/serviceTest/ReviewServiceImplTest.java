package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.coolSchool.enums.Role;
import com.coolSchool.coolSchool.exceptions.common.AccessDeniedException;
import com.coolSchool.coolSchool.exceptions.review.ReviewNotFoundException;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.common.ReviewDTO;
import com.coolSchool.coolSchool.models.dto.request.ReviewRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.CourseResponseDTO;
import com.coolSchool.coolSchool.models.dto.response.ReviewResponseDTO;
import com.coolSchool.coolSchool.models.entity.Course;
import com.coolSchool.coolSchool.models.entity.Review;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.repositories.CourseRepository;
import com.coolSchool.coolSchool.repositories.ReviewRepository;
import com.coolSchool.coolSchool.repositories.UserRepository;
import com.coolSchool.coolSchool.services.CourseService;
import com.coolSchool.coolSchool.services.impl.ReviewServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    PublicUserDTO publicUserDTO;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ReviewServiceImpl reviewService;
    @Mock
    private CourseService courseService;
    private ModelMapper modelMapper;
    @Mock
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        publicUserDTO = new PublicUserDTO(1L, "user", "user", "user@gmail.com", Role.USER, "description", false);
        modelMapper = new ModelMapper();
        reviewService = new ReviewServiceImpl(reviewRepository, courseService, userRepository, courseRepository, modelMapper, messageSource);
    }

    @Test
    public void testDeleteReview_ReviewPresent() {
        Long reviewId = 1L;

        User user = new User();
        user.setId(1L);

        Course course = new Course();
        course.setId(1L);

        Review review = new Review();
        review.setDeleted(false);
        review.setUser(user);
        review.setCourse(course);

        Optional<Review> reviewOptional = Optional.of(review);
        when(reviewRepository.findByIdAndDeletedFalse(reviewId)).thenReturn(reviewOptional);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        assertDoesNotThrow(() -> reviewService.deleteReview(reviewId, publicUserDTO));
        assertTrue(review.isDeleted());
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void testGetReviewById() {
        Long reviewId = 1L;
        Review review = new Review();
        Optional<Review> reviewOptional = Optional.of(review);
        when(reviewRepository.findByIdAndDeletedFalse(reviewId)).thenReturn(reviewOptional);
        ReviewDTO result = reviewService.getReviewById(reviewId);
        assertNotNull(result);
    }

    @Test
    void testGetReviewByIdNotFound() {
        Long reviewId = 1L;
        Optional<Review> reviewOptional = Optional.empty();
        when(reviewRepository.findByIdAndDeletedFalse(reviewId)).thenReturn(reviewOptional);
        assertThrows(ReviewNotFoundException.class, () -> reviewService.getReviewById(reviewId));
    }

    @Test
    void testCreateReview() {
        ReviewRequestDTO reviewDTO = new ReviewRequestDTO();

        User user = new User();
        user.setId(1L);

        Course course = new Course();
        course.setId(1L);

        Review review = new Review();
        review.setDeleted(false);
        review.setUser(user);
        review.setText("text");
        review.setCourse(course);
        review = modelMapper.map(reviewDTO, Review.class);

        when(userRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(user));
        when(courseRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(course));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ReviewDTO result = reviewService.createReview(reviewDTO, publicUserDTO);

        assertNotNull(result);
    }

    @Test
    void testDeleteReviewNotFound() {
        Long nonExistentReviewId = 99L;

        when(reviewRepository.findByIdAndDeletedFalse(nonExistentReviewId)).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () -> reviewService.deleteReview(nonExistentReviewId, publicUserDTO));
    }

    @Test
    void testCreateReview_ValidationException() {
        ReviewRequestDTO reviewDTO = new ReviewRequestDTO();
        reviewDTO.setText(null);

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);

        when(userRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new User()));
        when(courseRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Course()));
        when(reviewRepository.save(any(Review.class))).thenThrow(constraintViolationException);

        assertThrows(ConstraintViolationException.class, () -> reviewService.createReview(reviewDTO, publicUserDTO));
    }

    @Test
    void testCreateReview_ReviewNotFoundException() {
        ReviewRequestDTO reviewDTO = new ReviewRequestDTO();
        reviewDTO.setId(1L);
        reviewDTO.setText("Test Review");

        publicUserDTO.setRole(Role.ADMIN);

        when(userRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new User()));
        when(courseRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Course()));
        when(reviewRepository.save(any(Review.class))).thenThrow(ReviewNotFoundException.class);

        assertThrows(ReviewNotFoundException.class, () -> reviewService.createReview(reviewDTO, publicUserDTO));
    }

    @Test
    void testCreateReview_AccessDeniedException() {
        ReviewRequestDTO reviewDTO = new ReviewRequestDTO();
        reviewDTO.setId(1L);
        reviewDTO.setUserId(2L);
        reviewDTO.setText("Test Review");

        publicUserDTO.setRole(Role.TEACHER);

        when(userRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new User()));
        when(courseRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Course()));
        when(reviewRepository.save(any(Review.class))).thenThrow(AccessDeniedException.class);

        assertThrows(AccessDeniedException.class, () -> reviewService.createReview(reviewDTO, publicUserDTO));
    }

    @Test
    void testDeleteReview_AccessDeniedException() {
        ReviewRequestDTO reviewDTO = new ReviewRequestDTO();
        reviewDTO.setId(1L);
        reviewDTO.setUserId(2L);
        reviewDTO.setText("Test Review");

        publicUserDTO.setRole(Role.TEACHER);

        User user = new User();
        user.setId(1L);

        Course course = new Course();
        course.setId(1L);

        Review review = new Review();
        review.setDeleted(false);
        review.setUser(user);
        review.setCourse(course);

        when(reviewRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenThrow(AccessDeniedException.class);

        assertThrows(AccessDeniedException.class, () -> reviewService.deleteReview(1L, publicUserDTO));
    }

    @Test
    void testGetAllReviews() {
        Long courseId = 123L;

        CourseResponseDTO courseDTO = new CourseResponseDTO();
        List<Review> reviews = new ArrayList<>();

        when(courseService.getCourseById(courseId)).thenReturn(courseDTO);
        when(reviewRepository.findAllByCourse(any(Course.class))).thenReturn(reviews);

        List<ReviewResponseDTO> responseDTOs = reviewService.getAllReviews(courseId);

        assertNotNull(responseDTOs);
    }

    @Test
    void testCreateReview_WithNullLoggedUser_ThrowsAccessDeniedException() {
        ReviewRequestDTO reviewDTO = new ReviewRequestDTO();

        publicUserDTO = null;

        assertThrows(AccessDeniedException.class, () -> reviewService.createReview(reviewDTO, publicUserDTO));
    }

    @Test
    void testDeleteReview_WithNullLoggedUser_ThrowsAccessDeniedException() {
        Long id = 123L;

        publicUserDTO = null;

        when(reviewRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.of(new Review()));

        assertThrows(AccessDeniedException.class, () -> reviewService.deleteReview(id, publicUserDTO));
    }
}
