package fa.appcode.services.impl;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class VerificationService {
    private final Map<String, String> otpStorage = new HashMap<>();
    private final Map<String, Instant> otpExpiry = new HashMap<>();
    private static final long OTP_VALID_DURATION = 5 * 60;

    public String generateOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStorage.put(email, otp);
        otpExpiry.put(email, Instant.now().plusSeconds(OTP_VALID_DURATION));
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        if (!otpStorage.containsKey(email)) return false;
        if (otpExpiry.get(email).isBefore(Instant.now())) {
            otpStorage.remove(email);
            otpExpiry.remove(email);
            return false;
        }
        boolean isValid = otpStorage.get(email).equals(otp);
        otpStorage.remove(email);
        otpExpiry.remove(email);
        return isValid;
    }
}
//
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Service;
//import java.util.concurrent.TimeUnit;
//import java.util.Random;
//
//@Service
//public class VerificationService {
//    private final StringRedisTemplate redisTemplate;
//    private static final long OTP_VALID_DURATION = 5 * 60; // 5 phút
//    private static final int MAX_ATTEMPTS = 3; // Giới hạn số lần nhập sai OTP
//
//    public VerificationService(StringRedisTemplate redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    public String generateOtp(String email) {
//        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
//        String otpKey = "otp:" + email;
//        String attemptKey = "attempts:" + email;
//
//        // Lưu OTP vào Redis với TTL 5 phút
//        redisTemplate.opsForValue().set(otpKey, otp, OTP_VALID_DURATION, TimeUnit.SECONDS);
//
//        // Đặt lại số lần nhập sai
//        redisTemplate.opsForValue().set(attemptKey, "0", OTP_VALID_DURATION, TimeUnit.SECONDS);
//
//        return otp;
//    }
//
//    public boolean validateOtp(String email, String otp) {
//        String otpKey = "otp:" + email;
//        String attemptKey = "attempts:" + email;
//
//        String storedOtp = redisTemplate.opsForValue().get(otpKey);
//        if (storedOtp == null) {
//            return false; // OTP không tồn tại hoặc đã hết hạn
//        }
//
//        String attemptsStr = redisTemplate.opsForValue().get(attemptKey);
//        int attempts = attemptsStr == null ? 0 : Integer.parseInt(attemptsStr);
//
//        if (attempts >= MAX_ATTEMPTS) {
//            return false; // Quá số lần thử, chặn nhập OTP
//        }
//
//        boolean isValid = storedOtp.equals(otp);
//        if (isValid) {
//            // Xóa OTP khỏi Redis sau khi sử dụng
//            redisTemplate.delete(otpKey);
//            redisTemplate.delete(attemptKey);
//            return true;
//        } else {
//            // Tăng số lần nhập sai OTP
//            redisTemplate.opsForValue().increment(attemptKey);
//            return false;
//        }
//    }
//}
