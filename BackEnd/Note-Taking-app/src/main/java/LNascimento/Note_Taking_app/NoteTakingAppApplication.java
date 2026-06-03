package LNascimento.Note_Taking_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableScheduling
public class NoteTakingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoteTakingAppApplication.class, args);
	}
}
