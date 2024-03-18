package com.coolSchool.coolSchool.services;

import com.coolSchool.coolSchool.models.dto.common.CategoryDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface AIAssistanceService {
    String generateText(String inputText);

    String extractContent(String aiGeneratedContent);

    String analyzeContent(String prompt) throws JsonProcessingException;

    String buildPrompt(String blogContent, List<CategoryDTO> categories);

    CategoryDTO matchCategory(String aiResponse, List<CategoryDTO> categories);
}
