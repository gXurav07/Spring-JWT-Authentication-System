package authentication.service;

import authentication.entities.RefreshToken;
import authentication.entities.UserInfo;
import authentication.repository.RefreshTokenRepository;
import authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    UserRepository userRepository;

    public RefreshToken createRefreshToken(String username) {
        UserInfo userInfo = userRepository.findByUsername(username);
        RefreshToken refreshToken = refreshTokenRepository.findByUserInfo(userInfo)
                .orElseGet(() -> RefreshToken.builder()
                        .userInfo(userInfo)
                        .build());
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(1000 * 60 * 10)); // 10 min
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired");
        }
        return  token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

}
