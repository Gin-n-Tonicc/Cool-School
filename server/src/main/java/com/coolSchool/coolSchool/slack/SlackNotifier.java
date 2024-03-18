package com.coolSchool.coolSchool.slack;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * SlackNotifier is a component responsible for sending notifications to Slack channels.
 * It utilizes a webhook URL configured in the application properties to send messages to Slack.
 */
@Component
@Slf4j
public class SlackNotifier {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    @Value("${webhook.url}")
    private String webhookUrl;

    public void sendNotification(String message) {
        // Build the payload with the message
        Payload payload = Payload.builder()
                .text(message)
                .build();

        try {
            // Send the payload to Slack using the configured webhook URL
            Slack slack = Slack.getInstance();
            slack.send(webhookUrl, payload);
        } catch (IOException ex) {
            // Log any errors that occur during the notification process
            LocalDateTime timestamp = LocalDateTime.now();
            String time = String.format("%s%n", DATE_TIME_FORMATTER.format(timestamp));
            log.warn(time + ex.getMessage());
        }
    }
}
