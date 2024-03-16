package com.coolSchool.CoolSchool.serviceTest;


import com.coolSchool.coolSchool.exceptions.AI.ErrorProcessingAIResponseException;
import com.coolSchool.coolSchool.models.dto.common.CategoryDTO;
import com.coolSchool.coolSchool.services.impl.AIAssistanceServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AIAssistanceServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MessageSource messageSource;
    @Mock
    private ObjectMapper objectMapper;

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

    @Test
    void testMatchCategory_MatchingCategory() {
        String aiResponse = "Category A";
        List<CategoryDTO> categories = new ArrayList<>();
        categories.add(new CategoryDTO(1L, "Category A"));
        categories.add(new CategoryDTO(2L, "Category B"));

        CategoryDTO result = aiAssistanceService.matchCategory(aiResponse, categories);

        assertEquals(categories.get(0), result);
    }

    @Test
    void testMatchCategory_NoMatchingCategory() {
        String aiResponse = "Category C";
        List<CategoryDTO> categories = new ArrayList<>();
        categories.add(new CategoryDTO(1L, "Category A"));
        categories.add(new CategoryDTO(2L, "Category B"));

        assertThrows(ErrorProcessingAIResponseException.class, () -> aiAssistanceService.matchCategory(aiResponse, categories));
    }

    @Test
    void testBuildPrompt() {
        String blogContent = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        List<CategoryDTO> categories = new ArrayList<>();
        categories.add(new CategoryDTO(1L, "Category A"));
        categories.add(new CategoryDTO(2L, "Category B"));

        String prompt = aiAssistanceService.buildPrompt(blogContent, categories);

        StringBuilder expectedPromptBuilder = new StringBuilder();
        expectedPromptBuilder.append("Given the blog content:\n\n");
        expectedPromptBuilder.append(blogContent);
        expectedPromptBuilder.append("\n\nWhich of the following categories is most appropriate? Please provide the category name.\n\n");
        for (CategoryDTO category : categories) {
            expectedPromptBuilder.append("- ").append(category.getName()).append("\n");
        }
        String expectedPrompt = expectedPromptBuilder.toString();

        assertEquals(expectedPrompt, prompt);
    }

    @Test
    void testAnalyzeContent_ErrorProcessingAIResponseException() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(any(ObjectNode.class))).thenReturn("requestBody");

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(responseEntity);

        assertThrows(ErrorProcessingAIResponseException.class, () -> aiAssistanceService.analyzeContent("prompt"));
    }
}

