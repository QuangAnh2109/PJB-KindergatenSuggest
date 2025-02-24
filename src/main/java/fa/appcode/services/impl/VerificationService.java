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
