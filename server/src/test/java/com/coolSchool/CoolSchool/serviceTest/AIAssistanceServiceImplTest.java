package com.coolSchool.CoolSchool.serviceTest;


import com.coolSchool.coolSchool.exceptions.AI.ErrorProcessingAIResponseException;
import com.coolSchool.coolSchool.services.impl.AIAssistanceServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AIAssistanceServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private AIAssistanceServiceImpl aiAssistanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateText_Success() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("AI response", HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        String generatedText = aiAssistanceService.generateText("Input text");

        assertEquals("AI response", generatedText);
    }

    @Test
    void testGenerateText_Error() {
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new RuntimeException("Error"));

        String generatedText = aiAssistanceService.generateText("Input text");

        assertFalse(generatedText.startsWith("Error"));
    }

    @Test
    void testExtractContent_MissingContentNode() throws JsonProcessingException {
        String aiResponse = "{\"choices\":[{\"message\":{}}]}";

        assertThrows(ErrorProcessingAIResponseException.class, () -> {
            aiAssistanceService.extractContent(aiResponse);
        });
    }
}

