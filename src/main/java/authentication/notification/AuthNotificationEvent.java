package authentication.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthNotificationEvent {
    private String eventType;   // SIGNUP or LOGIN
    private String username;
    private String email;
    private String message;
}