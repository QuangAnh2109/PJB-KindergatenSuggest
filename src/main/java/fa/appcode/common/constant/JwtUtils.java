package fa.appcode.common.constant;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;


@Component
public class JwtUtils {
    @Value("jwt.secret")
    private String secret;

    private static final String SECRET_KEY ="00cf5cda3e007166811630233ba9c428aeccc5fff63daf246f6e63213bb37e7d" ; // Đổi thành khóa bí mật

    // Tạo JWT Token
    public String generateToken(String email) {
        long expirationTime = 15 * 60 * 1000; // 15 phút

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Giải mã token
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    // Lấy email từ token
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // Kiểm tra token còn hợp lệ không
    public boolean validateToken(String token) {
        try {
            return extractClaims(token).getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
