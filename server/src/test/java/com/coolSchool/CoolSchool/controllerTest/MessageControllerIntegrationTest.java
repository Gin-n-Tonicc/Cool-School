package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.CoolSchool.controllers.MessageController;
import com.coolSchool.CoolSchool.models.dto.MessageDTO;
import com.coolSchool.CoolSchool.services.impl.MessageServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = MessageController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = MessageController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE
                )
        }
)
class MessageControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MessageServiceImpl messageService;
    private List<MessageDTO> messageList;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        messageList = new ArrayList<>();
        messageList.add(new MessageDTO());
    }

    @Test
    void testGetAllMessages() throws Exception {
        Mockito.when(messageService.getAllMessages()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/messages/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetMessageById() throws Exception {
        Long messageId = 1L;
        MessageDTO message = new MessageDTO();

        Mockito.when(messageService.getMessageById(messageId)).thenReturn(message);

        mockMvc.perform(get("/api/v1/messages/{id}", messageId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateMessage() throws Exception {
        MessageDTO message = new MessageDTO();
        String messageJson = objectMapper.writeValueAsString(message);

        Mockito.when(messageService.createMessage(Mockito.any(MessageDTO.class))).thenReturn(message);

        mockMvc.perform(post("/api/v1/messages/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(messageJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateMessage() throws Exception {
        Long messageId = 1L;
        MessageDTO updatedMessage = new MessageDTO();
        String updatedMessageJson = objectMapper.writeValueAsString(updatedMessage);

        Mockito.when(messageService.updateMessage(Mockito.eq(messageId), Mockito.any(MessageDTO.class)))
                .thenReturn(updatedMessage);

        mockMvc.perform(put("/api/v1/messages/{id}", messageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedMessageJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteMessageById() throws Exception {
        Long messageId = 1L;
        mockMvc.perform(delete("/api/v1/messages/{id}", messageId))
                .andExpect(status().isOk());
    }
}
