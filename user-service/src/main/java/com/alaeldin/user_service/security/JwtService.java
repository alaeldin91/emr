package com.alaeldin.user_service.security;

import com.alaeldin.exception.not_found.ResourceNotFound;
import com.alaeldin.user_service.entity.Role;
import com.alaeldin.user_service.entity.User;
import com.alaeldin.user_service.repository.RoleRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * JWT Service for handling JWT token operations including generation, validation, and extraction of claims.
 * This service provides secure JWT token management with proper error handling and logging.
 *
 * @author Alaeldin
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {

    private static final String ROLE_CLAIM = "role";
    private static final String TOKEN_TYPE_CLAIM = "type";
    private static final String ACCESS_TOKEN_TYPE = "access";

    @Value("${jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String secretKey;

    @Value("${jwt.expiration:3600000}") // 1 hour default
    private Long jwtExpiration;

    @Value("${jwt.refresh-expiration:604800000}") // 7 days default
    private Long refreshExpiration;

    private final RoleRepository roleRepository;

    /**
     * Extracts the username (subject) from the JWT token
     *
     * @param token JWT token
     * @return username from token subject
     * @throws JwtException if token is invalid
     */
    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            log.error("Failed to extract username from token: {}", e.getMessage());
            throw new JwtException("Invalid token: unable to extract username");
        }
    }

    /**
     * Extracts the user ID from the JWT token
     *
     * @param token JWT token
     * @return user ID from token claims
     */
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    /**
     * Extracts the role from the JWT token
     *
     * @param token JWT token
     * @return role from token claims
     */
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get(ROLE_CLAIM, String.class));
    }

    /**
     * Extracts the expiration date from the JWT token
     *
     * @param token JWT token
     * @return expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts the issued at date from the JWT token
     *
     * @param token JWT token
     * @return issued at date
     */
    public Date extractIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    /**
     * Generic method to extract any claim from the JWT token
     *
     * @param token JWT token
     * @param claimsResolver function to resolve the specific claim
     * @param <T> type of the claim
     * @return extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates an access token for the user with role information
     *
     * @param userDetails user details
     * @param role user role
     * @return generated JWT token
     */
    public String generateAccessToken(User userDetails, Role role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetails.getId());
        claims.put(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE);
        return generateToken(claims, userDetails, role, jwtExpiration);
    }

    /**
     * Generates a refresh token for the user
     *
     * @param userDetails user details
     * @return generated refresh token
     */
    public String generateRefreshToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetails.getId());
        claims.put(TOKEN_TYPE_CLAIM, "refresh");
        return generateToken(claims, userDetails, null, refreshExpiration);
    }

    /**
     * Validates if the token is valid for the given user
     *
     * @param token JWT token
     * @param userDetails user details to validate against
     * @return true if token is valid, false otherwise
     */
    public boolean isTokenValid(String token, User userDetails) {
        try {
            final String username = extractUsername(token);
            return username.equals(userDetails.getEmail()) && !isTokenExpired(token);
        } catch (Exception e) {
            log.warn("Token validation failed for user {}: {}", userDetails.getEmail(), e.getMessage());
            return false;
        }
    }

    /**
     * Checks if the token is expired
     *
     * @param token JWT token
     * @return true if token is expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(Date.from(Instant.now()));
        } catch (ExpiredJwtException e) {
            log.debug("Token is expired: {}", e.getMessage());
            return true;
        } catch (Exception e) {
            log.error("Error checking token expiration: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Gets the remaining time until token expiration
     *
     * @param token JWT token
     * @return remaining time in milliseconds, or 0 if expired
     */
    public long getTokenRemainingTime(String token) {
        try {
            Date expiration = extractExpiration(token);
            long remaining = expiration.getTime() - System.currentTimeMillis();
            return Math.max(0, remaining);
        } catch (Exception e) {
            log.error("Error calculating token remaining time: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Validates token format and signature without checking expiration
     *
     * @param token JWT token
     * @return true if token format and signature are valid
     */
    public boolean isTokenFormatValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (SignatureException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.warn("Malformed JWT token: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.warn("JWT token compact of handler are invalid: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error validating token format: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extracts all claims from the JWT token
     *
     * @param token JWT token
     * @return all claims from the token
     * @throws JwtException if token is invalid
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.debug("Token expired: {}", e.getMessage());
            throw e;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw new JwtException("Invalid token signature");
        } catch (MalformedJwtException e) {
            log.error("Malformed JWT token: {}", e.getMessage());
            throw new JwtException("Malformed token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
            throw new JwtException("Unsupported token");
        } catch (IllegalArgumentException e) {
            log.error("JWT token compact of handler are invalid: {}", e.getMessage());
            throw new JwtException("Invalid token arguments");
        }
    }

    /**
     * Generates a JWT token with the specified claims and expiration
     *
     * @param extraClaims additional claims to include
     * @param userDetails user details
     * @param role user role (can be null for refresh tokens)
     * @param expiration expiration time in milliseconds
     * @return generated JWT token
     */
    private String generateToken(Map<String, Object> extraClaims, User userDetails, Role role, Long expiration) {
        try {
            if (role != null) {
                Role roleEntity = roleRepository.findByRoleName(role.getRoleName().getValue())
                        .orElseThrow(() -> new ResourceNotFound("Role", "roleName", Math.toIntExact(role.getId())));
                extraClaims.put(ROLE_CLAIM, roleEntity.getRoleName().getValue());
            }

            return Jwts.builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getEmail())
                    .setIssuedAt(Date.from(Instant.now()))
                    .setExpiration(Date.from(Instant.now().plusMillis(expiration)))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                    .compact();
        } catch (Exception e) {
            log.error("Error generating token for user {}: {}", userDetails.getEmail(), e.getMessage());
            throw new RuntimeException("Failed to generate token", e);
        }
    }

    /**
     * Gets the signing key for JWT token operations
     *
     * @return signing key
     */
    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 64) { // 512 bits minimum for HS512
            log.warn("JWT secret key length is less than recommended 512 bits");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Gets token expiration time as LocalDateTime
     *
     * @param token JWT token
     * @return expiration time as LocalDateTime
     */
    public Optional<LocalDateTime> getTokenExpirationAsLocalDateTime(String token) {
        try {
            Date expiration = extractExpiration(token);
            return Optional.of(LocalDateTime.ofInstant(expiration.toInstant(), ZoneId.systemDefault()));
        } catch (Exception e) {
            log.error("Error converting token expiration to LocalDateTime: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
