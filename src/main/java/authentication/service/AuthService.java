package authentication.service;

import authentication.model.AuthRequestDTO;
import authentication.notification.AuthNotificationProducer;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthNotificationProducer authNotificationProducer;

    public boolean loginUser(AuthRequestDTO authRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDTO.getUsername(),
                        authRequestDTO.getPassword()
                )
        );
        authNotificationProducer.sendLoginNotification(authRequestDTO.getUsername(), null);

        return authentication.isAuthenticated();
    }
}