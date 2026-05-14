package duy.hoang.server.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
     private String SECRET_KEY = "70fe03d6-353b-426c-bc3e-54e9756cdacb";

     public String generateToken(String email) {

          return Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(new Date())
                    .setExpiration(
                              new Date(System.currentTimeMillis()
                                        + 1000 * 60 * 60))
                    .signWith(
                              Keys.hmacShaKeyFor(
                                        SECRET_KEY.getBytes()),
                              SignatureAlgorithm.HS256)
                    .compact();
     }

     public String extractUserName(String token) {
          return extractAllClaims(token).getSubject();
     }

     private SecretKey getSignKey() {
          return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
     }

     private Claims extractAllClaims(String token) {

          return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
     }

     private boolean isTokenExpired(String token) {
          return extractAllClaims(token)
                    .getExpiration()
                    .before(new Date());
     }

     public boolean validateToken(String token, UserDetails userDetails) {
          final String username = extractUserName(token);

          return username.equals(userDetails.getUsername())
                    && !isTokenExpired(token);
     }
}
