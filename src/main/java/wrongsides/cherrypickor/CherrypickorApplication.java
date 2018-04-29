package wrongsides.cherrypickor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import wrongsides.cherrypickor.config.environment.Config;
import wrongsides.cherrypickor.config.environment.LocalConfig;

@SpringBootApplication
public class CherrypickorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CherrypickorApplication.class, args);
	}

	@Bean
	public Config getConfig() {
		return new LocalConfig();
	}
}
