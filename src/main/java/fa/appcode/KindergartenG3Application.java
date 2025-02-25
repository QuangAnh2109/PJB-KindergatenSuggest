package fa.appcode;

import fa.appcode.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class KindergartenG3Application {
	@Autowired
	private EmailService emailService;

	public static void main(String[] args) {
		var context = SpringApplication.run(KindergartenG3Application.class, args);
		KindergartenG3Application app = context.getBean(KindergartenG3Application.class);
//		app.sendEmailtoMe();
	}

//	public void sendEmailtoMe() {
//		String emailTo = "fapteam01@gmail.com";
//		String subject = "Kindergarten";
//		String text = "This is a simple email subject.";
//		emailService.sendEmail(emailTo, subject, text);
//	}
}

