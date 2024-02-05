package com.coolSchool.coolSchool.config.schedulers;

import com.coolSchool.coolSchool.models.entity.Token;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.models.entity.VerificationToken;
import com.coolSchool.coolSchool.repositories.TokenRepository;
import com.coolSchool.coolSchool.repositories.UserRepository;
import com.coolSchool.coolSchool.repositories.VerificationTokenRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@ComponentScan
@Component
@EnableScheduling
public class UserCleanupScheduler {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    public UserCleanupScheduler(UserRepository userRepository, TokenRepository tokenRepository, VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Scheduled(cron = "0 19 9 * * *") // Run every 24 hours
    public void deleteUnconfirmedUsers() {
        LocalDateTime thresholdDateTime = LocalDateTime.now().minusHours(24);
        List<User> unconfirmedUsers = userRepository.findByEnabledFalseAndCreatedAtBefore(thresholdDateTime);

        for (User user : unconfirmedUsers) {
            List<VerificationToken> userVerificationTokens = verificationTokenRepository.findByUserAndCreatedAtBefore(user, thresholdDateTime);

            for (VerificationToken verificationToken : userVerificationTokens) {
                verificationTokenRepository.delete(verificationToken);
            }

            List<Token> userTokens = tokenRepository.findAllByUser(user);
            for (Token token : userTokens) {
                tokenRepository.delete(token);
            }
        }

        userRepository.deleteAll(unconfirmedUsers);
    }
}

