package fa.appcode;

import fa.appcode.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KindergartenG3Application {
	public static void main(String[] args) {
		var context = SpringApplication.run(KindergartenG3Application.class, args);
		KindergartenG3Application app = context.getBean(KindergartenG3Application.class);
	}

}

