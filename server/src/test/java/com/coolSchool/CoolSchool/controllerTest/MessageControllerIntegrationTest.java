package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.MessageController;
import com.coolSchool.coolSchool.models.dto.common.MessageDTO;
import com.coolSchool.coolSchool.services.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageControllerIntegrationTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetAllMessages() {
        List<MessageDTO> messages = Arrays.asList(
                new MessageDTO(),
                new MessageDTO()
        );
        messages.get(0).setId(1L);
        messages.get(0).setContent("Message 1");
        messages.get(1).setId(2L);
        messages.get(1).setContent("Message 2");

        when(messageService.getAllMessages()).thenReturn(messages);

        ResponseEntity<List<MessageDTO>> response = messageController.getAllMessages();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(messages, response.getBody());
    }

    @Test
    void testGetMessageById() {
        MessageDTO message = new MessageDTO();
        message.setId(1L);
        message.setContent("Message 1");

        when(messageService.getMessageById(1L)).thenReturn(message);

        ResponseEntity<MessageDTO> response = messageController.getMessageById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    void testCreateMessage() {
        MessageDTO message = new MessageDTO();
        message.setId(1L);
        message.setContent("Message 1");;

        when(messageService.createMessage(message)).thenReturn(message);

        ResponseEntity<MessageDTO> response = messageController.createMessage(message);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    void testUpdateMessage() {
        MessageDTO message = new MessageDTO();
        message.setId(1L);
        message.setContent("Message 1");

        when(messageService.updateMessage(1L, message)).thenReturn(message);

        ResponseEntity<MessageDTO> response = messageController.updateMessage(1L, message);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    void testDeleteMessageById() {
        Long messageId = 1L;

        ResponseEntity<String> response = messageController.deleteMessageById(messageId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Message with id: " + messageId + " has been deleted successfully!", response.getBody());
        verify(messageService, times(1)).deleteMessage(messageId);
    }
}

