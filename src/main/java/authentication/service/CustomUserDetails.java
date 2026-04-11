package authentication.service;

import authentication.entities.UserInfo;
import authentication.entities.UserRole;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private UserInfo user;
    Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserInfo user) {
        this.user = user;
        this.authorities = user.getRoles().stream()
                .map(UserRole::getName)
                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                .toList();
    }

    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    @NullMarked
    public String getUsername() {
        return user.getUsername();
    }
}
