package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.AnswerController;
import com.coolSchool.coolSchool.models.dto.common.AnswerDTO;
import com.coolSchool.coolSchool.services.AnswerService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = AnswerController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = AnswerController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = AnswerController.class
                )
        }
)
class AnswerControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AnswerService answerService;
    private List<AnswerDTO> mockAnswerList;

    @BeforeEach
    void setUp() {
        mockAnswerList = Arrays.asList(
                new AnswerDTO(),
                new AnswerDTO()
        );
        mockAnswerList.get(0).setId(1L);
        mockAnswerList.get(0).setText("First answer");
        mockAnswerList.get(1).setId(2L);
        mockAnswerList.get(1).setText("Second answer");
    }

    @Test
    void testGetAllAnswers() throws Exception {
        when(answerService.getAllAnswers()).thenReturn(mockAnswerList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/answers/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(mockAnswerList.size()));
    }

    @Test
    void testGetAnswerById() throws Exception {
        Long answerId = 1L;
        AnswerDTO mockAnswer = new AnswerDTO();
        mockAnswer.setId(answerId);
        mockAnswer.setText("First answer");
        when(answerService.getAnswerById(answerId)).thenReturn(mockAnswer);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/answers/{id}", answerId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(answerId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(mockAnswer.getText()));
    }

    @Test
    void testCreateAnswer() throws Exception {
        AnswerDTO requestAnswer = new AnswerDTO();
        requestAnswer.setId(null);
        AnswerDTO mockAnswer = new AnswerDTO();
        mockAnswer.setId(1L);
        mockAnswer.setText("New answer");
        when(answerService.createAnswer(requestAnswer)).thenReturn(mockAnswer);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/answers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestAnswer)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(mockAnswer.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(mockAnswer.getText()));
    }

    @Test
    void testUpdateAnswer() throws Exception {
        Long answerId = 1L;
        AnswerDTO requestAnswer = new AnswerDTO();
        requestAnswer.setId(answerId);
        requestAnswer.setText("Updated answer");

        AnswerDTO mockUpdatedAnswer = new AnswerDTO();
        mockUpdatedAnswer.setId(answerId);
        mockUpdatedAnswer.setText("Updated answer");

        when(answerService.updateAnswer(answerId, requestAnswer)).thenReturn(mockUpdatedAnswer);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/answers/{id}", answerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestAnswer)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(answerId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("Updated answer"));
    }

    @Test
    void testDeleteAnswerById() throws Exception {
        Long answerId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/answers/{id}", answerId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Answer with id: " + answerId + " has been deleted successfully!"));

        verify(answerService, times(1)).deleteAnswer(answerId);
    }

}