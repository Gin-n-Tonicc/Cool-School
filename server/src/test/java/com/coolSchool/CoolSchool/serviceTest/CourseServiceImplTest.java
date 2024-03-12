package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.coolSchool.models.dto.response.CourseResponseDTO;
import com.coolSchool.coolSchool.models.entity.Course;
import com.coolSchool.coolSchool.repositories.CategoryRepository;
import com.coolSchool.coolSchool.repositories.CourseRepository;
import com.coolSchool.coolSchool.repositories.FileRepository;
import com.coolSchool.coolSchool.repositories.UserRepository;
import com.coolSchool.coolSchool.services.UserCourseService;
import com.coolSchool.coolSchool.services.impl.CourseServiceImpl;
import com.coolSchool.coolSchool.slack.SlackNotifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserCourseService userCourseService;

    @Mock
    private MessageSource messageSource;

    @Mock
    private SlackNotifier slackNotifier;

    @Mock
    private FileRepository fileRepository;

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
}
