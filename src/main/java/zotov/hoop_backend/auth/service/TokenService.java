package zotov.hoop_backend.auth.service;

import zotov.hoop_backend.credentials.repository.UserCredentialsRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final UserCredentialsRepository userCredentialsRepository;

    public TokenService(
            JwtEncoder jwtEncoder,
            UserCredentialsRepository userCredentialsRepository) {
        this.jwtEncoder = jwtEncoder;
        this.userCredentialsRepository = userCredentialsRepository;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        boolean mustChangePassword = userCredentialsRepository.findByEmail(authentication.getName())
                .map(credentials -> Boolean.TRUE.equals(credentials.getMustChangePassword()))
                .orElse(false);

        if (mustChangePassword) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Password change required before login");
        }
        
        String scope = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> !authority.startsWith("ROLE"))
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(authentication.getName())
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .claim("scope", scope)
                .build();

        JwtEncoderParameters parameters = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS512).build(),
                claims);

        return jwtEncoder.encode(parameters).getTokenValue();
    }
}
