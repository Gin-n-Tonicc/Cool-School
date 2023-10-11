package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.CoolSchool.exceptions.message.MessageNotFoundException;
import com.coolSchool.CoolSchool.exceptions.message.ValidationMessageException;
import com.coolSchool.CoolSchool.models.dto.MessageDTO;
import com.coolSchool.CoolSchool.models.entity.Message;
import com.coolSchool.CoolSchool.models.entity.User;
import com.coolSchool.CoolSchool.repositories.MessageRepository;
import com.coolSchool.CoolSchool.repositories.UserRepository;
import com.coolSchool.CoolSchool.services.impl.MessageServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessagesServiceImplTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    private ModelMapper modelMapper;
    @Mock
    private UserRepository userRepository;
    private Validator validator;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        messageService = new MessageServiceImpl(messageRepository, userRepository, modelMapper, validator);
    }

    @Test
    public void testDeleteMessage_MessagePresent() {
        Long messageId = 1L;

        Message message = new Message();
        message.setDeleted(false);

        Optional<Message> messageOptional = Optional.of(message);

        when(messageRepository.findByIdAndDeletedFalse(messageId)).thenReturn(messageOptional);
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        assertDoesNotThrow(() -> messageService.deleteMessage(messageId));
        assertTrue(message.isDeleted());
        verify(messageRepository, times(1)).save(message);
    }

    @Test
    void testGetAllMessages() {
        List<Message> messageList = new ArrayList<>();
        messageList.add(new Message());
        Mockito.when(messageRepository.findByDeletedFalse()).thenReturn(messageList);
        List<MessageDTO> result = messageService.getAllMessages();
        assertNotNull(result);
        assertEquals(messageList.size(), result.size());
    }

    @Test
    void testGetMessageById() {
        Long messageId = 1L;
        Message message = new Message();
        Optional<Message> messageOptional = Optional.of(message);
        when(messageRepository.findByIdAndDeletedFalse(messageId)).thenReturn(messageOptional);
        MessageDTO result = messageService.getMessageById(messageId);
        assertNotNull(result);
    }

    @Test
    void testGetMessageByIdNotFound() {
        Long messageId = 1L;
        Optional<Message> messageOptional = Optional.empty();
        when(messageRepository.findByIdAndDeletedFalse(messageId)).thenReturn(messageOptional);
        assertThrows(MessageNotFoundException.class, () -> messageService.getMessageById(messageId));
    }

    @Test
    void testCreateMessage() {
        MessageDTO messageDTO = new MessageDTO();
        Message message = modelMapper.map(messageDTO, Message.class);
        message.setContent("content");
        User user = new User();
        message.setReceiver(user);
        message.setSender(user);
        message.setSent_at(LocalDateTime.now());
        when(messageRepository.save(any(Message.class))).thenReturn(message);
        when(userRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new User()));
        MessageDTO result = messageService.createMessage(messageDTO);
        assertNotNull(result);
    }

    @Test
    void testUpdateMessage() {
        Long messageId = 1L;
        MessageDTO updatedMessageDTO = new MessageDTO();
        Message existingMessage = new Message();
        Optional<Message> existingMessageOptional = Optional.of(existingMessage);
        when(messageRepository.findByIdAndDeletedFalse(messageId)).thenReturn(existingMessageOptional);
        when(messageRepository.save(any(Message.class))).thenReturn(existingMessage);
        when(userRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new User()));
        MessageDTO result = messageService.updateMessage(messageId, updatedMessageDTO);
        assertNotNull(result);
    }

    @Test
    void testUpdateMessageNotFound() {
        Long nonExistentMessageId = 99L;
        MessageDTO updatedMessageDTO = new MessageDTO();
        when(messageRepository.findByIdAndDeletedFalse(nonExistentMessageId)).thenReturn(Optional.empty());
        assertThrows(MessageNotFoundException.class, () -> messageService.updateMessage(nonExistentMessageId, updatedMessageDTO));
    }

    @Test
    void testDeleteMessageNotFound() {
        Long nonExistentMessageId = 99L;

        when(messageRepository.findByIdAndDeletedFalse(nonExistentMessageId)).thenReturn(Optional.empty());

        assertThrows(MessageNotFoundException.class, () -> messageService.deleteMessage(nonExistentMessageId));
    }

    @Test
    void testCreateMessage_ValidationException() {
        MessageDTO messageDTO = new MessageDTO();

        messageDTO.setContent("content");
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);
        when(userRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new User()));
        when(messageRepository.save(any(Message.class))).thenThrow(constraintViolationException);

        assertThrows(ValidationMessageException.class, () -> messageService.createMessage(messageDTO));
    }

    @Test
    void testUpdateMessage_ValidationException() {
        Long messageId = 1L;
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent(null);
        Message existingMessage = new Message();
        Optional<Message> existingMessageOptional = Optional.of(existingMessage);

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);

        when(messageRepository.findByIdAndDeletedFalse(messageId)).thenReturn(existingMessageOptional);
        when(messageRepository.save(any(Message.class))).thenThrow(constraintViolationException);
        when(userRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new User()));
        assertThrows(ConstraintViolationException.class, () -> messageService.updateMessage(messageId, messageDTO));
    }
}
