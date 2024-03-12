package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.coolSchool.enums.Role;
import com.coolSchool.coolSchool.exceptions.review.ReviewNotFoundException;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private CourseService courseService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        userRepository = mock(UserRepository.class);
        courseRepository = mock(CourseRepository.class);
        messageSource = mock(MessageSource.class);

        reviewService = new ReviewServiceImpl(reviewRepository, courseService, userRepository, courseRepository, modelMapper, messageSource);
    }

    @Test
    void testGetAllReviews() {
        Long courseId = 1L;
        CourseResponseDTO courseDTO = new CourseResponseDTO();
        when(courseService.getCourseById(courseId)).thenReturn(courseDTO);

        List<Review> reviews = List.of(new Review(), new Review());
        when(reviewRepository.findAllByCourse(any())).thenReturn(reviews);

        List<ReviewResponseDTO> result = reviewService.getAllReviews(courseId);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetReviewById() {
        Long reviewId = 1L;
        Review review = new Review();
        when(reviewRepository.findByIdAndDeletedFalse(reviewId)).thenReturn(Optional.of(review));

        ReviewResponseDTO result = reviewService.getReviewById(reviewId);

        assertNotNull(result);
    }

    @Test
    void testDeleteReview() {
        Review review = new Review();
        review.setId(1L);
        review.setDeleted(false);
        review.setCourse(new Course());

        User user = new User();
        user.setId(1L);

        review.setUser(user);

        PublicUserDTO loggedUser = new PublicUserDTO();
        loggedUser.setId(1L);
        loggedUser.setRole(Role.USER);

        when(reviewRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(review));

        reviewService.deleteReview(1L, loggedUser);

        verify(reviewRepository, times(1)).save(review);
        assert (review.isDeleted());
    }

    @Test
    void testDeleteReview_WhenReviewNotFound() {
        PublicUserDTO loggedUser = new PublicUserDTO();
        loggedUser.setId(1L);

        when(reviewRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () -> reviewService.deleteReview(1L, loggedUser));
    }
}
