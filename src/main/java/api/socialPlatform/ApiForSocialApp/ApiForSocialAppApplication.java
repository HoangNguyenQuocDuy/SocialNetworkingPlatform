package api.socialPlatform.ApiForSocialApp;

import api.socialPlatform.ApiForSocialApp.model.User;
import api.socialPlatform.ApiForSocialApp.services.Impl.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableJpaRepositories
public class ApiForSocialAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiForSocialAppApplication.class, args);
	}
	@Bean
	CommandLineRunner run(UserServiceImpl userService) {
		return args -> {
			userService.saveUser(new User("user01", "q_duy", "123456", "asdbc"));
			userService.saveUser(new User("user02", "q_duy_2", "5151", "asdbcfasg"));
			userService.saveUser(new User("user03", "q_duy_3", "123456", "gsdg"));
		};
	}
}
