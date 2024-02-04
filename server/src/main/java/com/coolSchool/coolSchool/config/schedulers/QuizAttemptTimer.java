package com.coolSchool.coolSchool.config.schedulers;

import com.coolSchool.coolSchool.models.entity.QuizAttempt;
import com.coolSchool.coolSchool.repositories.QuizAttemptRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class QuizAttemptTimer {


    private final QuizAttemptRepository quizAttemptRepository;

    public QuizAttemptTimer(QuizAttemptRepository quizAttemptRepository) {
        this.quizAttemptRepository = quizAttemptRepository;
    }

    @Scheduled(fixedDelay = 60000)
    public void updateQuizAttemptsTimeLeft() {
        List<QuizAttempt> quizAttempts = quizAttemptRepository.findByCompletedFalse();
        for (QuizAttempt attempt : quizAttempts) {
            LocalDateTime startTime = attempt.getStartTime();
            Integer initialTimeLimit = attempt.getQuiz().getQuizDurationInMinutes();
            Integer timeElapsed = calculateTimeElapsed(startTime);
            int timeLeft = initialTimeLimit - timeElapsed;

            attempt.setTimeLeft(timeLeft);
            quizAttemptRepository.save(attempt);

            if (timeLeft <= 0) {
                attempt.setCompleted(true);
                quizAttemptRepository.save(attempt);
            }
        }
    }

    private int calculateTimeElapsed(LocalDateTime startTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime, currentTime);
        return (int) duration.toMinutes();
    }
}

