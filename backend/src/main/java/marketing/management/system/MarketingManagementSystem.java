package marketing.management.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class MarketingManagementSystem {

	public static void main(String[] args) {
		SpringApplication.run(MarketingManagementSystem.class, args);
	}

}
