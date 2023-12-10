package com.coolSchool.coolSchool.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users_answers")
public class UserAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "The user of the userAnswer should not be null!")
    private User user;
    @ManyToOne
    @JoinColumn(name = "answer_id")
    @NotNull(message = "The answer of the userAnswer should not be null!")
    private Answer answer;
    private Integer attemptNumber;
    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

}
