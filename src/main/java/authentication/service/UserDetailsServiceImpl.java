package authentication.service;

import authentication.entities.Roles;
import authentication.entities.UserInfo;
import authentication.entities.UserRole;
import authentication.model.UserInfoDTO;
import authentication.notification.AuthNotificationProducer;
import authentication.repository.RoleRepository;
import authentication.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthNotificationProducer authNotificationProducer;
    private final RoleRepository roleRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userRepository.findByUsername(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException("Could not find user: " + username + " !!");
        }
        return new CustomUserDetails(userInfo);
    }

    public boolean signupUser(UserInfoDTO userInfoDTO) {
        if (userExists(userInfoDTO.getUsername())) {
            return false;
        }
        UserRole defaultRole = roleRepository.findByName(Roles.USER.name())
                .orElseThrow(() -> new RuntimeException("Default role 'USER' not found"));

        String encodedPassword = passwordEncoder.encode(userInfoDTO.getPassword());
        userRepository.save(new UserInfo(null,
                userInfoDTO.getUsername(),
                encodedPassword,
                Set.of(defaultRole)));
        authNotificationProducer.sendSignupNotification(userInfoDTO.getUsername(), null);
        return true;
    }

    private boolean userExists(String username) {
        return  userRepository.findByUsername(username) != null;
    }
}

