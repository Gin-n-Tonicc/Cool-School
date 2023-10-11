package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.CoolSchool.exceptions.common.NoSuchElementException;
import com.coolSchool.CoolSchool.exceptions.userCourse.UserCourseAlreadyExistsException;
import com.coolSchool.CoolSchool.exceptions.userCourse.UserCourseNotFoundException;
import com.coolSchool.CoolSchool.models.dto.UserCourseDTO;
import com.coolSchool.CoolSchool.models.entity.User;
import com.coolSchool.CoolSchool.models.entity.UserCourse;
import com.coolSchool.CoolSchool.repositories.CourseRepository;
import com.coolSchool.CoolSchool.repositories.UserCourseRepository;
import com.coolSchool.CoolSchool.repositories.UserRepository;
import com.coolSchool.CoolSchool.services.impl.UserCourseServiceImpl;
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
    private Validator validator;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        userCourseService = new UserCourseServiceImpl(userCourseRepository, userRepository, courseRepository, modelMapper, validator);
    }

    @Test
    void testCreateUserCourseAlreadyExists() {
        UserCourseDTO userCourseDTO = new UserCourseDTO();
        userCourseDTO.setUserId(1L);
        userCourseDTO.setCourseId(2L);
        when(userCourseRepository.existsByUserIdAndCourseIdAndDeletedFalse(userCourseDTO.getUserId(), userCourseDTO.getCourseId()))
                .thenReturn(true);
        assertThrows(UserCourseAlreadyExistsException.class, () -> userCourseService.createUserCourse(userCourseDTO));
        verify(userCourseRepository, times(1)).existsByUserIdAndCourseIdAndDeletedFalse(userCourseDTO.getUserId(), userCourseDTO.getCourseId());
        verify(userRepository, never()).findByIdAndDeletedFalse(anyLong());
        verify(courseRepository, never()).findByIdAndDeletedFalse(anyLong());
        verify(userCourseRepository, never()).save(any(UserCourse.class));
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
        List<UserCourseDTO> result = userCourseService.getAllUserCourses();
        assertNotNull(result);
        assertEquals(UserCourseList.size(), result.size());
    }

    @Test
    void testGetUserCourseById() {
        Long UserCourseId = 1L;
        UserCourse UserCourse = new UserCourse();
        Optional<UserCourse> UserCourseOptional = Optional.of(UserCourse);
        when(userCourseRepository.findByIdAndDeletedFalse(UserCourseId)).thenReturn(UserCourseOptional);
        UserCourseDTO result = userCourseService.getUserCourseById(UserCourseId);
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
    void testCreateUserCourseUserNotFound() {
        UserCourseDTO userCourseDTO = new UserCourseDTO();
        userCourseDTO.setUserId(1L);
        userCourseDTO.setCourseId(2L);
        when(userRepository.findByIdAndDeletedFalse(userCourseDTO.getUserId())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userCourseService.createUserCourse(userCourseDTO));
    }

    @Test
    void testCreateUserCourseCourseNotFound() {
        UserCourseDTO userCourseDTO = new UserCourseDTO();
        userCourseDTO.setUserId(1L);
        userCourseDTO.setCourseId(2L);
        when(userRepository.findByIdAndDeletedFalse(userCourseDTO.getUserId())).thenReturn(Optional.of(new User()));
        when(courseRepository.findByIdAndDeletedFalse(userCourseDTO.getCourseId())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userCourseService.createUserCourse(userCourseDTO));
    }

    @Test
    void testUpdateUserCourseNotFound() {
        Long nonExistentUserCourseId = 99L;
        UserCourseDTO updatedUserCourseDTO = new UserCourseDTO();
        when(userCourseRepository.findByIdAndDeletedFalse(nonExistentUserCourseId)).thenReturn(Optional.empty());
        assertThrows(UserCourseNotFoundException.class, () -> userCourseService.updateUserCourse(nonExistentUserCourseId, updatedUserCourseDTO));
    }

    @Test
    void testDeleteUserCourseNotFound() {
        Long nonExistentUserCourseId = 99L;

        when(userCourseRepository.findByIdAndDeletedFalse(nonExistentUserCourseId)).thenReturn(Optional.empty());

        assertThrows(UserCourseNotFoundException.class, () -> userCourseService.deleteUserCourse(nonExistentUserCourseId));
    }

}

