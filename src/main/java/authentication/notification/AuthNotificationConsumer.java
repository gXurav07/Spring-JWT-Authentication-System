package authentication.notification;

import authentication.config.KafkaProducerConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthNotificationConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = KafkaProducerConfig.AUTH_NOTIFICATION_TOPIC, groupId = "auth-notification-group")
    public void consume(AuthNotificationEvent event) {
        log.info("Received auth notification event: {}", event);

        String subject;
        String body;

        if ("SIGNUP".equals(event.getEventType())) {
            subject = "Welcome to the platform";
            body = "Hello " + event.getUsername() + ",\n\n"
                    + "Your account has been created successfully.\n\n"
                    + "Thanks,\nAuth Service Team";
        } else if ("LOGIN".equals(event.getEventType())) {
            subject = "Login successful";
            body = "Hello " + event.getUsername() + ",\n\n"
                    + "You have logged in successfully.\n\n"
                    + "Thanks,\nAuth Service Team";
        } else {
            log.warn("Unknown event type: {}", event.getEventType());
            return;
        }

        System.out.println(subject + "\n\n" + body);

        if (event.getEmail() == null || event.getEmail().isBlank()) {
            log.warn("Skipping mail send because recipient email is missing for event: {}", event);
            return;
        }

        try {
            emailService.sendSimpleEmail(event.getEmail(), subject, body);
        } catch (Exception e) {
            System.out.println("Failed to send mail!\n" + e);
        }

    }
}
