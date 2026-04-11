package authentication.controller;

import authentication.entities.RefreshToken;
import authentication.model.AuthRequestDTO;
import authentication.model.JwtResponseDTO;
import authentication.model.RefreshAccessTokenRequestDTO;
import authentication.model.UserInfoDTO;
import authentication.service.AuthService;
import authentication.service.JwtService;
import authentication.service.RefreshTokenService;
import authentication.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/auth/v1/")
public class AuthController
{

    @Autowired private JwtService jwtService;

    @Autowired private RefreshTokenService refreshTokenService;

    @Autowired private UserDetailsServiceImpl userDetailsService;

    @Autowired private AuthService authService;

    @PostMapping("signup")
    public ResponseEntity signup(@RequestBody UserInfoDTO userInfoDto) {
        if(!userDetailsService.signupUser(userInfoDto)){
            return new ResponseEntity<>("Already Exist", HttpStatus.BAD_REQUEST);
        }

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
        String jwtToken = jwtService.generateToken(userInfoDto.getUsername());
        return new ResponseEntity<>(JwtResponseDTO.builder().accessToken(jwtToken).
                token(refreshToken.getToken()).build(), HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthRequestDTO authRequestDTO){
        if(!authService.loginUser(authRequestDTO)) {
            return new ResponseEntity<>("Login failed!", HttpStatus.UNAUTHORIZED);
        }

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
        return new ResponseEntity<>(JwtResponseDTO.builder()
                .accessToken(jwtService.generateToken(authRequestDTO.getUsername()))
                .token(refreshToken.getToken())
                .build(), HttpStatus.OK);
    }

    @PostMapping("refreshAccessToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshAccessTokenRequestDTO refreshTokenRequestDTO){
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshToken -> {
                    try {
                        RefreshToken validRefreshToken = refreshTokenService.verifyExpiration(refreshToken);
                        String accessToken = jwtService.generateToken(validRefreshToken.getUserInfo().getUsername());
                        return ResponseEntity.ok(
                                JwtResponseDTO.builder()
                                        .accessToken(accessToken)
                                        .token(refreshTokenRequestDTO.getToken())
                                        .build()
                        );
                    } catch (RuntimeException exception) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Map.of("message", exception.getMessage()));
                    }
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Refresh token not found")));
    }

}
