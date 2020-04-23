package me.clevecord.scrum;

import me.clevecord.scrum.domain.user.entities.User;
import me.clevecord.scrum.domain.user.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import java.util.Date;
import java.util.UUID;

@SpringBootApplication
@EnableJdbcHttpSession
public class ScrumBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScrumBoardApplication.class, args);
	}

}
