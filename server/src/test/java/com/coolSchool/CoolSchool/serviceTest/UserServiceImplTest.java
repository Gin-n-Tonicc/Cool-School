package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.CoolSchool.exceptions.user.UserCreateException;
import com.coolSchool.CoolSchool.exceptions.user.UserNotFoundException;
import com.coolSchool.CoolSchool.models.dto.RegisterRequest;
import com.coolSchool.CoolSchool.models.entity.User;
import com.coolSchool.CoolSchool.repositories.UserRepository;
import com.coolSchool.CoolSchool.services.impl.UserServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testCreateUser() {
        RegisterRequest request = new RegisterRequest();
        User user = new User();
        String encodedPassword = "encodedPassword";

        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(request);

        verify(passwordEncoder, times(1)).encode(request.getPassword());
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
    public void testCreateUser_DataIntegrityViolationException() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "john@example.com", "password");

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException(""));

        assertThrows(UserCreateException.class, () -> userService.createUser(request));

        verify(passwordEncoder).encode(request.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testCreateUser_ConstraintViolationException() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "john@example.com", "password");

        Set constraintViolations = Collections.singleton(mock(ConstraintViolation.class));
        ConstraintViolationException exception = new ConstraintViolationException("Constraint violation", constraintViolations);

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenThrow(exception);

        assertThrows(UserCreateException.class, () -> userService.createUser(request));

        verify(passwordEncoder).encode(request.getPassword());
        verify(userRepository).save(any(User.class));
    }
}