package authentication.service;

import authentication.entities.UserInfo;
import authentication.model.UserInfoDTO;
import authentication.notification.AuthNotificationProducer;
import authentication.repository.UserRepository;
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
    private final UserRepository userRepository;

    public boolean loginUser(UserInfoDTO userInfoDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userInfoDTO.getUsername(),
                        userInfoDTO.getPassword()
                )
        );
        UserInfo userInfo = userRepository.findByUsername(userInfoDTO.getUsername());
        String email = userInfo != null ? userInfo.getEmail() : userInfoDTO.getEmail();
        authNotificationProducer.sendLoginNotification(userInfoDTO.getUsername(), email);

        return authentication.isAuthenticated();
    }
}
