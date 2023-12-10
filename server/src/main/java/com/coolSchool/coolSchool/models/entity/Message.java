package com.coolSchool.coolSchool.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "The sender of the quiz should not be null!")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @NotNull(message = "The receiver of the quiz should not be null!")
    private User receiver;
    //    private boolean isGroupChat;
    private LocalDateTime sent_at;
    @NotBlank(message = "The content of the quiz should not be blank!")
    @NotNull(message = "The content of the message should not be null!")
    private String content;
    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;
}
