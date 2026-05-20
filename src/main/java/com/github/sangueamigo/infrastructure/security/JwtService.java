package com.github.sangueamigo.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    // geracao de tokens
    public String gerarAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities()
                .iterator().next().getAuthority());
        return buildToken(claims, userDetails.getUsername(), expiration);
    }

    public String gerarRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails.getUsername(), refreshExpiration);
    }

    // Token de curta duração exclusivo para reset de senha
    public String gerarResetToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("type", "reset")          // claim para distinguir do access token
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15min
                .signWith(getSigningKey())
                .compact();
    }

    private String buildToken(Map<String, Object> claims, String subject, Long expiracao) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiracao))
                .signWith(getSigningKey())
                .compact();
    }

    // validacoes
    public boolean isTokenValido(String token, UserDetails userDetails) {
        final String email = extrairEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpirado(token);
    }

    public boolean isTokenExpirado(String token) {
        return extrairExpiracao(token).before(new Date());
    }

    public boolean isResetTokenValido(String token, UserDetails userDetails) {
        final String username = extrairEmail(token);
        final String type = extrairClaim(token, claims -> claims.get("type", String.class));

        return username.equals(userDetails.getUsername())
                && "reset".equals(type)
                && !isTokenExpirado(token);
    }

    // Extracoes de informacoes do token
    public String extrairEmail(String token) {
        return extrairClaim(token, claims -> claims.getSubject());
    }

    public String extrairRole(String token) {
        return extrairClaim(token, claims -> claims.get("role", String.class));
    }

    private Date extrairExpiracao(String token) {
        return extrairClaim(token, claims -> claims.getExpiration());
    }

    public <T> T extrairClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extrairTodosClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extrairTodosClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // key
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}