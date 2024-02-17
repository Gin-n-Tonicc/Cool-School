package com.coolSchool.coolSchool.config.schedulers;

import com.coolSchool.coolSchool.models.entity.Quiz;
import com.coolSchool.coolSchool.models.entity.QuizAttempt;
import com.coolSchool.coolSchool.repositories.QuizAttemptRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@ComponentScan
@Component
@EnableScheduling
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

    @Scheduled(fixedDelay = 30000)
    public void calculateRemainingTimeInSeconds() {
        List<QuizAttempt> quizAttempts = quizAttemptRepository.findAll();
        LocalDateTime currentTime = LocalDateTime.now();

        for (QuizAttempt quizAttempt : quizAttempts) {
            Quiz quiz = quizAttempt.getQuiz();

            if (quiz != null && quizAttempt.getStartTime() != null && quiz.getQuizDurationInMinutes() != null) {
                int quizDurationInSeconds = quiz.getQuizDurationInMinutes() * 60;
                LocalDateTime quizEndTime = quizAttempt.getStartTime().plusSeconds(quizDurationInSeconds);

                Duration timeElapsed = Duration.between(currentTime, quizEndTime);

                if (timeElapsed.isNegative()) {
                    quizAttempt.setRemainingTimeInSeconds(0L);
                } else {
                    long remainingSeconds = timeElapsed.getSeconds();
                    quizAttempt.setRemainingTimeInSeconds(remainingSeconds);
                }
            } else {
                quizAttempt.setRemainingTimeInSeconds(null);
            }

            quizAttemptRepository.save(quizAttempt);
        }
    }

    private int calculateTimeElapsed(LocalDateTime startTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime, currentTime);
        return (int) duration.toMinutes();
    }
}

