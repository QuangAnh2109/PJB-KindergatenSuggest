package fa.appcode.common.utils;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

/**
 * Utility class to build different types of email templates.
 * This class provides static methods to generate {@link SendMailInfo} objects
 * for various use cases such as registration confirmation and password reset.
 */
public class EmailBuilder {

    // Private constructor to prevent instantiation of this utility class.
    private EmailBuilder() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Builds the email information for account registration.
     * This email typically contains a verification link to confirm the user's email address.
     *
     * @param email The recipient's email address.
     * @return A {@link SendMailInfo} object containing all the necessary data for sending the registration email.
     */
    public static SendMailInfo buildRegistrationMail(String email) {
        String token = TokenUtils.generateTokenRegister(email);
        String verifiedLink = Constant.REGISTER_VERIFY_URL + token;

        Map<Placeholder, String> placeholders = Map.of(Placeholder.LINK, verifiedLink);

        return SendMailInfo.builder()
                .toMail(Collections.singletonList(email))
                .mailId(Constant.SEND_EMAIL_REGISTER)
                .ccMail(Collections.emptyList())
                .detail(placeholders)
                .build();
    }

    /**
     * Builds the email information for password reset.
     * This email contains a link allowing the user to reset their password.
     *
     * @param email     The recipient's email address.
     * @return A {@link SendMailInfo} object containing all the necessary data for sending the password reset email.
     */
    public static SendMailInfo buildForgotPasswordMail(String email, Instant changePasswordTime) {
        String token = TokenUtils.generateTokenForgot(email, changePasswordTime);
        String resetLink = Constant.RESET_PASSWORD_URL + token;
        Map<Placeholder, String> placeholders = Map.of(Placeholder.LINK, resetLink);
        return SendMailInfo.builder()
                .toMail(Collections.singletonList(email))
                .mailId(Constant.SEND_EMAIL_FORGOT)
                .ccMail(Collections.emptyList())
                .detail(placeholders)
                .build();
    }
}
