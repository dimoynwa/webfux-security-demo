package dev.dimo.webflux.security.webfluxsecurity.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final JwtEncoder jwtEncoder;

    public String generateToken(Authentication authentication) {
        log.info("Token requested for user {}", authentication.getName());
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        return jwtEncoder.encode(JwtEncoderParameters.from(JwtClaimsSet.builder()
                        .issuer("self")
                        .expiresAt(now.plus(15, ChronoUnit.MINUTES))
                        .subject(authentication.getName())
                        .issuedAt(now)
                        .claim("scope", scope)
                .build())).getTokenValue();
    }
}
