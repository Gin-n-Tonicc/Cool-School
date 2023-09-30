package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.MessageDTO;

import java.util.List;

public interface MessageService {
    List<MessageDTO> getAllMessages();

    MessageDTO getMessageById(Long id);

    MessageDTO createMessage(MessageDTO messageDTO);

    MessageDTO updateMessage(Long id, MessageDTO messageDTO);

    void deleteMessage(Long id);
}