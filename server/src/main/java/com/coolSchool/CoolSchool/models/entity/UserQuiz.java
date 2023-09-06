package com.coolSchool.CoolSchool.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_quizzes")
public class UserQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "The user of the userQuiz should not be null!")
    private User user;
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    @NotNull(message = "The quiz of the userQuiz should not be null!")
    private Quiz quiz;
    @NotNull(message = "The grade of the userQuiz should not be null!")
    private BigDecimal grade;
    @NotNull(message = "The feedback of the userQuiz should not be null!")
    private String feedback;
    @NotNull(message = "The completedAt time of the userQuiz should not be null!")
    private LocalDateTime completedAt;
    private BigDecimal totalMarks;
    private Integer attemptNumber;
    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;
}
