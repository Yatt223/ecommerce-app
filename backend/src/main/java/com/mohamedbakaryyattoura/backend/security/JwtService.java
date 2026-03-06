package com.mohamedbakaryyattoura.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // ✅ Valeurs directement dans le code (plus de @Value)
    private static final String SECRET =
            "ecommerce_secret_key_super_longue_au_moins_256_bits_pour_hs256";
    private static final long EXPIRATION = 86400000L; // 24h

    // Génère un token pour un utilisateur
    public String genererToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getCle())
                .compact();
    }

    // Vérifie si le token est valide
    public boolean estValide(String token, UserDetails userDetails) {
        final String email = extraireEmail(token);
        return email.equals(userDetails.getUsername())
                && !estExpire(token);
    }

    // Extrait l'email du token
    public String extraireEmail(String token) {
        return extraireClaim(token, Claims::getSubject);
    }

    // Vérifie si le token est expiré
    private boolean estExpire(String token) {
        return extraireClaim(token, Claims::getExpiration)
                .before(new Date());
    }

    // Méthode générique pour extraire un claim
    private <T> T extraireClaim(
            String token, Function<Claims, T> resolver) {
        final Claims claims = extraireTousClaims(token);
        return resolver.apply(claims);
    }

    private Claims extraireTousClaims(String token) {
        return Jwts.parser()
                .verifyWith(getCle())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getCle() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
}