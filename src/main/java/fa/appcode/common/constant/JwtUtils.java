package fa.appcode.common.constant;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String secret;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email) {
        long expirationTime = 15 * 60 * 1000;
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            return extractClaims(token).getExpiration().after(new Date());
        } catch (ExpiredJwtException e) {
            System.out.println("Token hết hạn: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Token không hợp lệ: " + e.getMessage());
            return false;
        }
    }
}
