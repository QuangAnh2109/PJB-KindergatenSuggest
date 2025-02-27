package fa.appcode.services;

import java.util.Map;

public interface EmailService {
        void sendEmailToOne(String mail, Integer id, Map<String, Object> detail);
        void sendEmailToMany(String[] mail, Integer id, Map<String, Object> detail);
}