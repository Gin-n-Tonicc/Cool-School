package com.coolSchool.coolSchool.services.impl;

import com.coolSchool.coolSchool.exceptions.AI.ErrorProcessingAIResponseException;
import com.coolSchool.coolSchool.exceptions.AI.UnableToExtractContentFromAIResponseException;
import com.coolSchool.coolSchool.services.AIAssistanceService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AIAssistanceServiceImpl implements AIAssistanceService {

    private final RestTemplate restTemplate;
    private final String openAIEndpoint = "https://api.openai.com/v1/chat/completions";
    private final String apiKey;
    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;

    public AIAssistanceServiceImpl(RestTemplate restTemplate, @Value("${openai.api.key}") String apiKey, MessageSource messageSource, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.messageSource = messageSource;
        this.objectMapper = objectMapper;
    }

    @Override
    public String generateText(String inputText) {
        String requestBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"" + inputText + "\"}]}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(openAIEndpoint, requestEntity, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            } else {
                return "Error: " + responseEntity.getStatusCodeValue() + " - " + responseEntity.getBody();
            }
        } catch (Exception e) {
            return "Exception occurred: " + e.getMessage();
        }
    }

    public String extractContent(String aiGeneratedContent) {
        try {
            JsonNode rootNode = objectMapper.readTree(aiGeneratedContent);
            JsonNode messageContent = rootNode.path("choices").get(0).path("message").path("content");
            if (!messageContent.isMissingNode()) {
                return messageContent.asText();
            } else {
                throw new UnableToExtractContentFromAIResponseException(messageSource);
            }
        } catch (Exception e) {
            throw new ErrorProcessingAIResponseException(messageSource);
        }
    }
}


