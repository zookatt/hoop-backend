package zotov.hoop_backend.auth.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.server.ResponseStatusException;
import zotov.hoop_backend.credentials.entity.UserCredentials;
import zotov.hoop_backend.credentials.repository.UserCredentialsRepository;
import zotov.hoop_backend.role.entity.UserRole;
import zotov.hoop_backend.user.entity.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @Test
    void generateTokenRejectsUsersThatMustChangePassword() {
        TokenService service = new TokenService(jwtEncoder, userCredentialsRepository);
        UserCredentials credentials = new UserCredentials(
                "worker@hoop.test",
                "encoded",
                new User(new UserRole("CLEANING")));

        when(userCredentialsRepository.findByEmail("worker@hoop.test")).thenReturn(Optional.of(credentials));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.generateToken(authentication()));

        assertEquals(409, exception.getStatusCode().value());
    }

    @Test
    void generateTokenReturnsEncodedJwt() {
        TokenService service = new TokenService(jwtEncoder, userCredentialsRepository);
        UserCredentials credentials = new UserCredentials(
                "worker@hoop.test",
                "encoded",
                new User(new UserRole("CLEANING")));
        credentials.setMustChangePassword(false);

        Jwt jwt = Jwt.withTokenValue("jwt-token")
                .header("alg", "HS512")
                .claim("scope", "CLEANING")
                .build();

        when(userCredentialsRepository.findByEmail("worker@hoop.test")).thenReturn(Optional.of(credentials));
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        String token = service.generateToken(authentication());

        assertEquals("jwt-token", token);
        verify(jwtEncoder).encode(any(JwtEncoderParameters.class));
    }

    private UsernamePasswordAuthenticationToken authentication() {
        return new UsernamePasswordAuthenticationToken(
                "worker@hoop.test",
                "password",
                List.of(new SimpleGrantedAuthority("CLEANING")));
    }
}
