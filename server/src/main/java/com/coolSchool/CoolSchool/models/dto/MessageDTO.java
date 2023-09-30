package com.coolSchool.CoolSchool.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private Long id;
    private String content;
    private Long senderId;
    private Long receiverId;
//    private Long groupId;
    private LocalDateTime sent_at;
}