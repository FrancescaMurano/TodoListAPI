package com.todolist.app.service;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

    // Chiave segreta per firmare i JWT (generata nel costruttore)
    private String secretkey = "";

    // Costruttore della classe: genera una chiave segreta casuale
    public JWTService() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256"); // Usa l'algoritmo HMAC SHA256
            SecretKey sk = keyGen.generateKey(); // Genera la chiave segreta
            secretkey = Base64.getEncoder().encodeToString(sk.getEncoded()); // Converte la chiave in Base64
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e); // Lancia un'eccezione in caso di errore
        }
    }

    // Metodo per generare un token JWT a partire da un username
    public String generateToken(String username, Set<String> role) {
        Map<String, Object> claims = new HashMap<>(); // Mappa dei claims (dati extra nel token)
        claims.put("role", role);  // Aggiunge il ruolo dell'utente
        claims.put("createdAt", new Date()); // Aggiunge la data di creazione

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) 
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Metodo per ottenere la chiave segreta a partire dalla stringa Base64
    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretkey); // Decodifica la chiave Base64
        return Keys.hmacShaKeyFor(keyBytes); // Crea una chiave HMAC con i byte decodificati
    }

    // Estrae lo username (subject) dal token
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Metodo generico per estrarre un claim specifico dal token
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token); // Ottiene tutti i claims dal token
        return claimResolver.apply(claims); // Applica la funzione di estrazione
    }

    // Estrae tutti i claims dal token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder() 
                    .setSigningKey(getKey()) 
                    .build()
                    .parseClaimsJws(token) 
                    .getBody();
}

    // Metodo per validare il token confrontandolo con i dati dell'utente
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token); // Estrae lo username dal token
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token)); // Verifica username e scadenza
    }

    // Controlla se il token è scaduto
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Confronta la data di scadenza con quella attuale
    }

    // Estrae la data di scadenza dal token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username) // L'username dell'utente come subject
                .setIssuedAt(new Date()) // Data di creazione
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7 giorni di validità
                .signWith(getKey(), SignatureAlgorithm.HS256) // Firma il token con la chiave segreta
                .compact();
    }

    public String refreshToken(Map<String, String> refreshToken) {
        try {
            // Verifica che il refresh token sia valido
            String username = extractUserName(refreshToken.get("refreshToken"));
            if (isTokenExpired(refreshToken.get("refreshToken"))) {
                throw new RuntimeException("Refresh token scaduto! Devi fare il login di nuovo.");
            }
            
            // Se è valido, genera un nuovo access token
            return generateToken(username, Set.of("ROLE_USER")); // Puoi modificare il ruolo in base alle necessità
    
        } catch (Exception e) {
            throw new RuntimeException("Refresh Token non valido!", e);
        }
    }
}
