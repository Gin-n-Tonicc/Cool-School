package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.AuthenticationController;
import com.coolSchool.coolSchool.models.dto.auth.AuthenticationRequest;
import com.coolSchool.coolSchool.models.dto.auth.AuthenticationResponse;
import com.coolSchool.coolSchool.models.dto.auth.RegisterRequest;
import com.coolSchool.coolSchool.services.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration
@WebMvcTest(value = AuthenticationController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = AuthenticationController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE
                )
        }
)
class AuthenticationControllerIntegrationTest {
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
    void setUp() {
        registerRequest = new RegisterRequest();
        authenticationRequest = new AuthenticationRequest();
        authenticationResponse = new AuthenticationResponse();
    }

    @Test
    void testRegister() throws Exception {
        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(authenticationResponse);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(authenticationResponse)));
    }

    @Test
    void testAuthenticate() throws Exception {
        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(authenticationResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testRefreshTokenEndpoint() throws Exception {
        String refreshTokenJson = "{\"refreshToken\": \"your_refresh_token_here\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshTokenJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testMeEndpoint() throws Exception {
        String accessTokenJson = "{\"accessToken\": \"your_access_token_here\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accessTokenJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
