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

    @Scheduled(cron = "0 00 0 * * *") // Run every 24 hours
    public void deleteUnconfirmedUsers() {
        LocalDateTime thresholdDateTime = LocalDateTime.now().minusHours(24);

        // Retrieve unconfirmed users created before the threshold date time
        List<User> unconfirmedUsers = userRepository.findByEnabledFalseAndCreatedAtBefore(thresholdDateTime);

        for (User user : unconfirmedUsers) {
            List<VerificationToken> userVerificationTokens = verificationTokenRepository.findByUserAndCreatedAtBefore(user, thresholdDateTime);
            verificationTokenRepository.deleteAll(userVerificationTokens);

            List<Token> userTokens = tokenRepository.findAllByUser(user);
            tokenRepository.deleteAll(userTokens);
            userRepository.deleteAll(unconfirmedUsers);
        }
    }
}


