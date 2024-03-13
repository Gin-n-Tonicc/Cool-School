package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.request.UserCourseRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.CourseResponseDTO;
import com.coolSchool.coolSchool.models.entity.Course;
import com.coolSchool.coolSchool.repositories.CourseRepository;
import com.coolSchool.coolSchool.services.UserCourseService;
import com.coolSchool.coolSchool.services.impl.CourseServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserCourseService userCourseService;

    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private CourseServiceImpl courseService;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        modelMapper = new ModelMapper();
    }

    @Test
    void getAllCourses_ReturnsNonEmptyList() {
        when(courseRepository.findByDeletedFalseOrderByCreatedDateDesc()).thenReturn(List.of(new Course()));

        List<CourseResponseDTO> courses = courseService.getAllCourses();

        assertFalse(courses.isEmpty());
    }

    @Test
    void canEnrollCourse_UserNotEnrolled_ReturnsTrue() {
        Long courseId = 1L;
        PublicUserDTO loggedUser = new PublicUserDTO();
        loggedUser.setId(1L);
        when(userCourseService.getAllUserCourses()).thenReturn(List.of());

        boolean canEnroll = courseService.canEnrollCourse(courseId, loggedUser);

        Assertions.assertTrue(canEnroll);
    }

    @Test
    void enrollCourse_ValidInput_EnrollsUser() {
        Long courseId = 1L;
        PublicUserDTO loggedUser = new PublicUserDTO();
        loggedUser.setId(1L);
        UserCourseRequestDTO userCourseRequestDTO = new UserCourseRequestDTO();
        userCourseRequestDTO.setCourseId(courseId);
        userCourseRequestDTO.setUserId(loggedUser.getId());

        courseService.enrollCourse(courseId, loggedUser);

        verify(userCourseService, times(1)).createUserCourse(userCourseRequestDTO);
    }
}
