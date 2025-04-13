package fa.appcode.services;

import fa.appcode.common.utils.SendMailInfo;

public interface EmailService {
    void sendEmailToMany(SendMailInfo sendMailInfo);
}
