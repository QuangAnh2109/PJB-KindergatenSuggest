package fa.appcode.common.utils;

import fa.appcode.entities.AccountInfo;
import fa.appcode.exceptions.TokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Component
public class TokenUtils {
    @Value("${token.key}")
    private String tokenKey;
    @Value("${token.time}")
    private String expirationTime;

    public String generateTokenForgot(String email, Instant passwordChange) {
        long expireAt = Instant.now().getEpochSecond() + Long.parseLong(expirationTime);
        String passwordChangeEpoch = (passwordChange != null) ? String.valueOf(passwordChange.getEpochSecond()) : "0";
        return encodeToken(tokenKey, email, String.valueOf(expireAt), passwordChangeEpoch);
    }
    public String generateTokenRegister(String email) {
        return encodeToken(tokenKey, email);
    }

    private String encodeToken(String... parts) {
        return Base64.getEncoder().encodeToString(String.join("|", parts).getBytes(StandardCharsets.UTF_8));
    }

    private String[] parseToken(String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
            return decoded.split("\\|");
        } catch (IllegalArgumentException e) {
            throw new TokenException(Constant.INVALID_TOKEN_FORMAT, e);
        }
    }

    public String getEmailFromToken(String token) {
        String[] parts = parseToken(token);
        if (parts.length < 2) throw new TokenException(Constant.INVALID_TOKEN);
        return parts[1];
    }

    public long getExpiredTime(String token) {
        String[] parts = parseToken(token);
        if (parts.length < 3) throw new TokenException(Constant.INVALID_TOKEN);
        try {
            return Long.parseLong(parts[2]);
        } catch (NumberFormatException e) {
            throw new TokenException(Constant.INVALID_TOKEN_FORMAT, e);
        }
    }
    public boolean isTokenValid(String token, AccountInfo account) {
        long expiredTime = getExpiredTime(token);
        return Instant.now().getEpochSecond() <= expiredTime && !isTokenUsed(account, expiredTime);
    }

public boolean isTokenUsed(AccountInfo account, long expiredTime) {
    if (account.getDatetimeChangePass() == null) {
        return false;
    }
    return account.getDatetimeChangePass().getEpochSecond() >= (expiredTime - Long.parseLong(expirationTime));
}

}
