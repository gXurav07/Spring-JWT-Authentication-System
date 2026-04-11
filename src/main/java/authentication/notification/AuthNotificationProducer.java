package authentication.notification;


import authentication.config.KafkaProducerConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthNotificationProducer {

    private final KafkaTemplate<String, AuthNotificationEvent> kafkaTemplate;

    public void sendSignupNotification(String username, String email) {
        AuthNotificationEvent event = new AuthNotificationEvent(
                "SIGNUP",
                username,
                email,
                "Welcome to our platform, " + username + "!"
        );
        kafkaTemplate.send(KafkaProducerConfig.AUTH_NOTIFICATION_TOPIC, username, event);
    }

    public void sendLoginNotification(String username, String email) {
        AuthNotificationEvent event = new AuthNotificationEvent(
                "LOGIN",
                username,
                email,
                "You have logged in successfully."
        );
        kafkaTemplate.send(KafkaProducerConfig.AUTH_NOTIFICATION_TOPIC, username, event);
    }
}
