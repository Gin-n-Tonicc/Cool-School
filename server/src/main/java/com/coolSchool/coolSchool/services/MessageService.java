package com.coolSchool.coolSchool.services;

import com.coolSchool.coolSchool.models.dto.common.MessageDTO;

import java.util.List;

public interface MessageService {
    List<MessageDTO> getAllMessages();

    MessageDTO getMessageById(Long id);

    MessageDTO createMessage(MessageDTO messageDTO);

    MessageDTO updateMessage(Long id, MessageDTO messageDTO);

    void deleteMessage(Long id);
}