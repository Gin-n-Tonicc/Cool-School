package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.coolSchool.exceptions.userCourse.UserCourseNotFoundException;
import com.coolSchool.coolSchool.models.dto.request.UserCourseRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.UserCourseResponseDTO;
import com.coolSchool.coolSchool.models.entity.UserCourse;
import com.coolSchool.coolSchool.repositories.CourseRepository;
import com.coolSchool.coolSchool.repositories.UserCourseRepository;
import com.coolSchool.coolSchool.repositories.UserRepository;
import com.coolSchool.coolSchool.services.impl.UserCourseServiceImpl;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCourseServiceImplTest {

    @Mock
    private UserCourseRepository userCourseRepository;

    @InjectMocks
    private UserCourseServiceImpl userCourseService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CourseRepository courseRepository;

    private ModelMapper modelMapper;
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        userCourseService = new UserCourseServiceImpl(userCourseRepository, userRepository, courseRepository, modelMapper, messageSource);
    }

    @Test
    public void testDeleteUserCourse_UserCoursePresent() {
        Long UserCourseId = 1L;

        UserCourse UserCourse = new UserCourse();
        UserCourse.setDeleted(false);

        Optional<UserCourse> UserCourseOptional = Optional.of(UserCourse);

        when(userCourseRepository.findByIdAndDeletedFalse(UserCourseId)).thenReturn(UserCourseOptional);
        when(userCourseRepository.save(any(UserCourse.class))).thenReturn(UserCourse);

        assertDoesNotThrow(() -> userCourseService.deleteUserCourse(UserCourseId));
        assertTrue(UserCourse.isDeleted());
        verify(userCourseRepository, times(1)).save(UserCourse);
    }

    @Test
    void testGetAllUserCourses() {
        List<UserCourse> UserCourseList = new ArrayList<>();
        UserCourseList.add(new UserCourse());
        Mockito.when(userCourseRepository.findByDeletedFalse()).thenReturn(UserCourseList);
        List<UserCourseResponseDTO> result = userCourseService.getAllUserCourses();
        assertNotNull(result);
        assertEquals(UserCourseList.size(), result.size());
    }

    @Test
    void testGetUserCourseById() {
        Long UserCourseId = 1L;
        UserCourse UserCourse = new UserCourse();
        Optional<UserCourse> UserCourseOptional = Optional.of(UserCourse);
        when(userCourseRepository.findByIdAndDeletedFalse(UserCourseId)).thenReturn(UserCourseOptional);
        UserCourseResponseDTO result = userCourseService.getUserCourseById(UserCourseId);
        assertNotNull(result);
    }

    @Test
    void testGetUserCourseByIdNotFound() {
        Long UserCourseId = 1L;
        Optional<UserCourse> UserCourseOptional = Optional.empty();
        when(userCourseRepository.findByIdAndDeletedFalse(UserCourseId)).thenReturn(UserCourseOptional);
        assertThrows(UserCourseNotFoundException.class, () -> userCourseService.getUserCourseById(UserCourseId));
    }

    @Test
    void testUpdateUserCourseNotFound() {
        Long nonExistentUserCourseId = 99L;
        UserCourseResponseDTO updatedUserCourseDTO = new UserCourseResponseDTO();
        when(userCourseRepository.findByIdAndDeletedFalse(nonExistentUserCourseId)).thenReturn(Optional.empty());
        assertThrows(UserCourseNotFoundException.class, () -> userCourseService.updateUserCourse(nonExistentUserCourseId, modelMapper.map(updatedUserCourseDTO, UserCourseRequestDTO.class)));
    }

    @Test
    void testDeleteUserCourseNotFound() {
        Long nonExistentUserCourseId = 99L;

        when(userCourseRepository.findByIdAndDeletedFalse(nonExistentUserCourseId)).thenReturn(Optional.empty());

        assertThrows(UserCourseNotFoundException.class, () -> userCourseService.deleteUserCourse(nonExistentUserCourseId));
    }

}

