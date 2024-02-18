package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.coolSchool.enums.Role;
import com.coolSchool.coolSchool.exceptions.common.AccessDeniedException;
import com.coolSchool.coolSchool.exceptions.user.UserCreateException;
import com.coolSchool.coolSchool.exceptions.user.UserNotFoundException;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.auth.RegisterRequest;
import com.coolSchool.coolSchool.models.dto.request.CompleteOAuthRequest;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.repositories.UserRepository;
import com.coolSchool.coolSchool.services.impl.UserServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private MessageSource messageSource;

    @Test
    void testCreateUser() {
        RegisterRequest request = new RegisterRequest();
        User user = new User();

        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(request);

        verify(userRepository, times(1)).save(any(User.class));
        Assertions.assertEquals(user, createdUser);
    }

    @Test
    void testFindByEmail() {
        String userEmail = "user@example.com";
        User user = new User();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        User foundUser = userService.findByEmail(userEmail);

        verify(userRepository, times(1)).findByEmail(userEmail);
        Assertions.assertEquals(user, foundUser);
    }

    @Test
    void testFindByEmail_UserNotFound() {
        String userEmail = "nonexistent@example.com";

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByEmail(userEmail));

        verify(userRepository, times(1)).findByEmail(userEmail);
    }

    @Test
    void testCreateUser_DataIntegrityViolationException() {
        RegisterRequest request = new RegisterRequest();

        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException(""));

        assertThrows(UserCreateException.class, () -> userService.createUser(request));

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreateUser_ConstraintViolationException() {
        RegisterRequest request = new RegisterRequest();

        Set constraintViolations = Collections.singleton(mock(ConstraintViolation.class));
        ConstraintViolationException exception = new ConstraintViolationException("Constraint violation", constraintViolations);

        when(userRepository.save(any(User.class))).thenThrow(exception);

        assertThrows(UserCreateException.class, () -> userService.createUser(request));

        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_DuplicateEmail_ThrowsUserCreateException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("john.doe@example.com");

        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(UserCreateException.class, () -> userService.createUser(request));
    }


    @Test
    void testDeleteUserById_AccessDeniedException() {
        Long userId = 1L;
        PublicUserDTO currentUser = new PublicUserDTO();
        currentUser.setId(1L);
        User user = new User();
        user.setId(currentUser.getId());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        assertThrows(AccessDeniedException.class, () -> userService.deleteUserById(userId, currentUser));
        verify(userRepository).findById(userId);
    }

    @Test
    void deleteUserById_Success() {
        Long userId = 1L;
        PublicUserDTO currentUser = new PublicUserDTO();
        currentUser.setId(2L);

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setDeleted(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        userService.deleteUserById(userId, currentUser);

        Assertions.assertTrue(mockUser.isDeleted());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void updateOAuth2UserWithFullDataTest() {
        CompleteOAuthRequest request = new CompleteOAuthRequest();
        request.setFirstname("John");
        request.setLastname("Doe");
        request.setAddress("123 Main St");
        request.setDescription("Sample description");
        request.setRole(Role.USER);

        Long userId = 1L;
        User existingUser = new User();
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(existingUser));

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userService.updateOAuth2UserWithFullData(request, userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));

        Assertions.assertEquals("John", updatedUser.getFirstname());
        Assertions.assertEquals("Doe", updatedUser.getLastname());
        Assertions.assertEquals("123 Main St", updatedUser.getAddress());
        Assertions.assertEquals("Sample description", updatedUser.getDescription());
    }
}