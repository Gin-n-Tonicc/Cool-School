package com.coolSchool.coolSchool.controllers;

import com.coolSchool.coolSchool.models.dto.common.MessageDTO;
import com.coolSchool.coolSchool.services.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<MessageDTO>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageDTO> getMessageById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(messageService.getMessageById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<MessageDTO> createMessage(@Valid @RequestBody MessageDTO messageDTO) {
        MessageDTO cratedMessage = messageService.createMessage(messageDTO);
        return new ResponseEntity<>(cratedMessage, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageDTO> updateMessage(@PathVariable("id") Long id, @Valid @RequestBody MessageDTO messageDTO) {
        return ResponseEntity.ok(messageService.updateMessage(id, messageDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessageById(@PathVariable("id") Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok("Message with id: " + id + " has been deleted successfully!");
    }
}
