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
    private final Map<String, Integer> otpAttempts = new HashMap<>();

    private static final long OTP_VALID_DURATION = 5 * 60;
    private static final int MAX_ATTEMPTS = 4;

    public String generateOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStorage.put(email, otp);
        otpExpiry.put(email, Instant.now().plusSeconds(OTP_VALID_DURATION));
        otpAttempts.put(email, 0);
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        if (!otpStorage.containsKey(email)) {
            return false;
        }
        if (otpExpiry.get(email).isBefore(Instant.now())) {
            otpStorage.remove(email);
            otpExpiry.remove(email);
            otpAttempts.remove(email);
            return false;
        }
        if (!otpStorage.get(email).equals(otp)) {

            otpAttempts.put(email, otpAttempts.getOrDefault(email, 0) + 1);
            if (otpAttempts.get(email) >= MAX_ATTEMPTS) {
            }
            return false;
        }
        otpStorage.remove(email);
        otpExpiry.remove(email);
        otpAttempts.remove(email);
        return true;
    }


    public boolean isOtpValid(String email) {
        if (!otpExpiry.containsKey(email)) {
            return false;
        }

        Instant expiry = otpExpiry.get(email);
        if (expiry.isBefore(Instant.now())) {
            return false;
        }

        return true;
    }
}
