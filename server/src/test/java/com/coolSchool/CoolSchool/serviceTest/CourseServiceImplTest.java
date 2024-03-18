package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.coolSchool.config.FrontendConfig;
import com.coolSchool.coolSchool.enums.Role;
import com.coolSchool.coolSchool.exceptions.common.AccessDeniedException;
import com.coolSchool.coolSchool.exceptions.course.CourseNotFoundException;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.request.CourseRequestDTO;
import com.coolSchool.coolSchool.models.dto.request.UserCourseRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.CourseResponseDTO;
import com.coolSchool.coolSchool.models.entity.Category;
import com.coolSchool.coolSchool.models.entity.Course;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.repositories.CategoryRepository;
import com.coolSchool.coolSchool.repositories.CourseRepository;
import com.coolSchool.coolSchool.repositories.FileRepository;
import com.coolSchool.coolSchool.repositories.UserRepository;
import com.coolSchool.coolSchool.services.UserCourseService;
import com.coolSchool.coolSchool.services.impl.CourseServiceImpl;
import com.coolSchool.coolSchool.slack.SlackNotifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
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
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private SlackNotifier slackNotifier;
    @Mock
    private FileRepository fileRepository;
    private PublicUserDTO publicUserDTO;
    @Mock
    private FrontendConfig frontendConfig;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        modelMapper = new ModelMapper();
        publicUserDTO = new PublicUserDTO(1L, "user", "user", "user@gmail.com", Role.USER, "description", false);
        courseService = new CourseServiceImpl(courseRepository, modelMapper, userRepository, categoryRepository, userCourseService, messageSource, slackNotifier, fileRepository, frontendConfig);
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

    @Test
    public void testDeleteCourse_AccessDenied() {
        Course course = new Course();
        course.setId(1L);
        course.setUser(new User());

        when(courseRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(course));

        assertThrows(AccessDeniedException.class, () -> {
            courseService.deleteCourse(1L, publicUserDTO);
        });

        verify(courseRepository, never()).save(course);
    }

    @Test
    public void testDeleteCourse_CourseNotFound() {
        when(courseRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> {
            courseService.deleteCourse(1L, publicUserDTO);
        });
        verify(courseRepository, never()).save(any());
    }

    @Test
    public void testGetCourseById_Success() {
        Course course = new Course();
        course.setId(1L);
        when(courseRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(course));

        CourseResponseDTO responseDTO = courseService.getCourseById(1L);

        Assertions.assertEquals(course.getId(), responseDTO.getId());
    }

    @Test
    public void testGetCourseById_CourseNotFound() {
        when(courseRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> {
            courseService.getCourseById(1L);
        });
    }

    @Test
    public void testCreateCourse_AccessDenied() {
        CourseRequestDTO courseDTO = new CourseRequestDTO();

        assertThrows(AccessDeniedException.class, () -> {
            courseService.createCourse(courseDTO, publicUserDTO);
        });
    }

    @Test
    void testUpdateCourse_Success() {
        PublicUserDTO publicUserDTO = new PublicUserDTO();
        publicUserDTO.setId(1L);

        CourseRequestDTO courseRequestDTO = new CourseRequestDTO();
        courseRequestDTO.setId(1L);
        courseRequestDTO.setUserId(publicUserDTO.getId());
        courseRequestDTO.setCategoryId(1L);

        Course existingCourse = new Course();
        existingCourse.setId(courseRequestDTO.getId());

        when(userRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new User()));
        when(categoryRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Category()));
        when(courseRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CourseResponseDTO responseDTO = courseService.updateCourse(courseRequestDTO.getId(), courseRequestDTO, publicUserDTO);

        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(courseRequestDTO.getId(), responseDTO.getId());
    }

    @Test
    void testSendSlackNotification() {
        Long userId = 1L;
        Long categoryId = 1L;
        Long courseId = 1L;
        String courseName = "Course 1";
        String authorFirstname = "John";
        String authorLastname = "Doe";
        String categoryName = "Category 1";
        String expectedMessage = "New course created in Cool School:\n" +
                "Name: " + courseName + "\n" +
                "Author: " + authorFirstname + " " + authorLastname + "\n" +
                "Category: " + categoryName + "\n" +
                "Read more: " + frontendConfig.getBaseUrl() + "/courses/" + courseId;

        User author = new User();
        author.setId(userId);
        author.setFirstname(authorFirstname);
        author.setLastname(authorLastname);

        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        Course course = new Course();
        course.setId(courseId);
        course.setName(courseName);
        course.setUser(author);
        course.setCategory(category);

        when(userRepository.findById(userId)).thenReturn(Optional.of(author));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        courseService.sendSlackNotification(course);

        verify(slackNotifier, times(1)).sendNotification(expectedMessage);
    }
}
