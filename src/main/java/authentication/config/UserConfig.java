package authentication.config;

import authentication.entities.Roles;
import authentication.entities.UserRole;
import authentication.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserConfig {

    private final RoleRepository roleRepository;

    public UserConfig(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner initRoles() {
        return args -> {
            if (roleRepository.findByName(Roles.USER.name()).isEmpty()) {
                roleRepository.save(new UserRole(null, Roles.USER.name()));
            }
            if (roleRepository.findByName(Roles.ADMIN.name()).isEmpty()) {
                roleRepository.save(new UserRole(null, Roles.ADMIN.name()));
            }
        };
    }

}
