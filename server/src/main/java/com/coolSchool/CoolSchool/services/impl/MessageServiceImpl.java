package com.coolSchool.CoolSchool.services.impl;

import com.coolSchool.CoolSchool.exceptions.common.NoSuchElementException;
import com.coolSchool.CoolSchool.exceptions.message.MessageNotFoundException;
import com.coolSchool.CoolSchool.exceptions.message.ValidationMessageException;
import com.coolSchool.CoolSchool.models.dto.MessageDTO;
import com.coolSchool.CoolSchool.models.entity.Message;
import com.coolSchool.CoolSchool.repositories.MessageRepository;
import com.coolSchool.CoolSchool.repositories.UserRepository;
import com.coolSchool.CoolSchool.services.MessageService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;

import java.util.List;
import java.util.Optional;
@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final Validator validator;

    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository, ModelMapper modelMapper, Validator validator) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }


    @Override
    public List<MessageDTO> getAllMessages() {
        List<Message> messages = messageRepository.findByDeletedFalse();
        return messages.stream().map(message -> modelMapper.map(message, MessageDTO.class)).toList();
    }

    @Override
    public MessageDTO getMessageById(Long id) {
        Optional<Message> message = messageRepository.findByIdAndDeletedFalse(id);
        if (message.isPresent()) {
            return modelMapper.map(message.get(), MessageDTO.class);
        }
        throw new MessageNotFoundException();
    }

    @Override
    public MessageDTO createMessage(MessageDTO messageDTO) {
        try {
            messageDTO.setId(null);
            userRepository.findByIdAndDeletedFalse(messageDTO.getReceiverId()).orElseThrow(NoSuchElementException::new);
            userRepository.findByIdAndDeletedFalse(messageDTO.getSenderId()).orElseThrow(NoSuchElementException::new);
            Message messageEntity = messageRepository.save(modelMapper.map(messageDTO, Message.class));
            return modelMapper.map(messageEntity, MessageDTO.class);
        } catch (ConstraintViolationException exception) {
            throw new ValidationMessageException(exception.getConstraintViolations());
        }
    }

    @Override
    public MessageDTO updateMessage(Long id, MessageDTO messageDTO) {
        Optional<Message> existingMessageOptional = messageRepository.findByIdAndDeletedFalse(id);

        if (existingMessageOptional.isEmpty()) {
            throw new MessageNotFoundException();
        }
        userRepository.findByIdAndDeletedFalse(messageDTO.getReceiverId()).orElseThrow(NoSuchElementException::new);
        userRepository.findByIdAndDeletedFalse(messageDTO.getSenderId()).orElseThrow(NoSuchElementException::new);
        Message existingMessage = existingMessageOptional.get();
        modelMapper.map(messageDTO, existingMessage);

        try {
            existingMessage.setId(id);
            Message updatedMessage = messageRepository.save(existingMessage);
            return modelMapper.map(updatedMessage, MessageDTO.class);
        } catch (TransactionException exception) {
            if (exception.getRootCause() instanceof ConstraintViolationException validationException) {
                throw new ValidationMessageException(validationException.getConstraintViolations());
            }
            throw exception;
        }
    }

    @Override
    public void deleteMessage(Long id) {
        Optional<Message> message = messageRepository.findByIdAndDeletedFalse(id);
        if (message.isPresent()) {
            message.get().setDeleted(true);
            messageRepository.save(message.get());
        } else {
            throw new MessageNotFoundException();
        }
    }
}