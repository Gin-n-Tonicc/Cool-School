package com.coolSchool.coolSchool.models.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "quiz_attempts")
public class QuizAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "quizAttempt", cascade = CascadeType.ALL)
    private List<UserAnswer> userAnswers;

    private BigDecimal totalMarks;

    private int attemptNumber;

    @Column(name = "quiz_completion_time")
    private LocalDateTime quizCompletionTime;

    private LocalDateTime startTime;

    private Integer timeLeft;

    private Long remainingTimeInSeconds;


    @Column(name = "completed", nullable = false)
    private boolean completed;
}
