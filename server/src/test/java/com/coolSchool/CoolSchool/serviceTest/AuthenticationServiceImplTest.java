package com.coolSchool.CoolSchool.serviceTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import com.coolSchool.CoolSchool.models.dto.AuthenticationRequest;
import com.coolSchool.CoolSchool.models.dto.AuthenticationResponse;
import com.coolSchool.CoolSchool.models.dto.RegisterRequest;
import com.coolSchool.CoolSchool.models.entity.User;
import com.coolSchool.CoolSchool.services.AuthenticationService;
import com.coolSchool.CoolSchool.services.JwtService;
import com.coolSchool.CoolSchool.services.TokenService;
import com.coolSchool.CoolSchool.services.UserService;
import com.coolSchool.CoolSchool.services.impl.AuthenticationServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.io.IOException;


public class AuthenticationServiceImplTest {
    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;

    @Mock
    private JwtService jwtService;
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationServiceImpl(
                userService,
                tokenService,
                jwtService,
                authenticationManager,
                modelMapper
        );
    }

    @Test
    void testRegister() {
        RegisterRequest registerRequest = new RegisterRequest();
        User user = new User();

        when(userService.createUser(any(RegisterRequest.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("mockedAccessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("mockedRefreshToken");

        AuthenticationResponse response = authenticationService.register(registerRequest);

        Assertions.assertNotNull(response);
        assertEquals("mockedAccessToken", response.getAccessToken());
        assertEquals("mockedRefreshToken", response.getRefreshToken());
    }

    @Test
    void testAuthenticate() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        User user = new User();

        when(userService.findByEmail(any(String.class))).thenReturn(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword()
        );
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);

        when(jwtService.generateToken(user)).thenReturn("mockedAccessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("mockedRefreshToken");

        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);

        Assertions.assertNotNull(response);
    }

    @Test
    void testRefreshToken() throws IOException {
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);
        User user = new User(/* initialize with necessary fields */);
        user.setEmail("email@gmail.com");
        String refreshToken = "mockedRefreshToken";

        when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + refreshToken);
        when(jwtService.extractUsername(refreshToken)).thenReturn(user.getEmail());
        when(userService.findByEmail(user.getEmail())).thenReturn(user);
        when(jwtService.isTokenValid(refreshToken, user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("mockedAccessToken");

        AuthenticationResponse response = authenticationService.refreshToken(mockRequest, mockResponse);

        Assertions.assertNotNull(response);
        assertEquals("mockedAccessToken", response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());

    }
}
