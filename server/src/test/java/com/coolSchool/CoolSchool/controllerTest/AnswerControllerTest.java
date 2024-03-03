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

import static org.mockito.Mockito.when;

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
class AnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnswerService answerService;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
}