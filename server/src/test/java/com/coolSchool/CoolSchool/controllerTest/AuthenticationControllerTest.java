package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.CoolSchool.controllers.AuthenticationController;
import com.coolSchool.CoolSchool.models.dto.AuthenticationRequest;
import com.coolSchool.CoolSchool.models.dto.AuthenticationResponse;
import com.coolSchool.CoolSchool.models.dto.RegisterRequest;
import com.coolSchool.CoolSchool.services.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = AuthenticationController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = AuthenticationController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE
//                        value = LogoutHandler.class
                )
        }
)
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    private RegisterRequest registerRequest;
    private AuthenticationRequest authenticationRequest;
    private AuthenticationResponse authenticationResponse;

    @BeforeEach
    public void setUp() {
        registerRequest = new RegisterRequest();
        authenticationRequest = new AuthenticationRequest();
        authenticationResponse = new AuthenticationResponse();
    }

    @Test
    public void testRegister() throws Exception {
        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(authenticationResponse);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(authenticationResponse)));
    }

    @Test
    public void testAuthenticate() throws Exception {
        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(authenticationResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(authenticationResponse)));
    }

    @Test
    public void testRefreshToken() throws Exception {
        when(authenticationService.refreshToken(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class))).thenReturn(authenticationResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/refresh-token"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(authenticationResponse)));
    }
}
