package org.corodiak.kcss1devt1auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KcsS1DevT1AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(KcsS1DevT1AuthApplication.class, args);
	}

}
