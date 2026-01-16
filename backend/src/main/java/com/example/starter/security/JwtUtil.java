package com.example.starter.security;

import com.example.starter.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

/**
 * English: Utility for generating and validating JWT tokens.
 * Italiano: Utility per generare e validare token JWT.
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {

  private final AppProperties properties;

  /**
   * English: Generates a signed JWT for the given user.
   * Italiano: Genera un JWT firmato per l'utente dato.
   */
  public String generateToken(AppUserDetails userDetails) {
    Instant now = Instant.now();
    Instant expiry = now.plusSeconds(properties.getJwt().getExpirationMinutes() * 60);

    return Jwts.builder()
      .setSubject(String.valueOf(userDetails.getId()))
      .claim("email", userDetails.getUsername())
      .claim("roles", userDetails.getRoles())
      .setIssuedAt(Date.from(now))
      .setExpiration(Date.from(expiry))
      .signWith(getSigningKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  /**
   * English: Extracts the user id from the token.
   * Italiano: Estrae lo user id dal token.
   */
  public Long extractUserId(String token) {
    return Long.parseLong(getAllClaims(token).getSubject());
  }

  /**
   * English: Validates token ownership and expiration.
   * Italiano: Valida proprieta del token e scadenza.
   */
  public boolean isTokenValid(String token, AppUserDetails userDetails) {
    return userDetails.getId().equals(extractUserId(token)) && !isTokenExpired(token);
  }

  /**
   * English: Checks token expiration.
   * Italiano: Controlla la scadenza del token.
   */
  private boolean isTokenExpired(String token) {
    return getAllClaims(token).getExpiration().before(new Date());
  }

  /**
   * English: Parses token and returns all JWT claims.
   * Italiano: Parsea il token e ritorna tutte le claim JWT.
   */
  private Claims getAllClaims(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(getSigningKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  /**
   * English: Builds the HMAC signing key from the configured secret.
   * Italiano: Crea la chiave di firma HMAC dal segreto configurato.
   */
  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(properties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8));
  }
}
