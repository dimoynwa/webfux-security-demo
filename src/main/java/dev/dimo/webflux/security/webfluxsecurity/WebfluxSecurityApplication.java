package dev.dimo.webflux.security.webfluxsecurity;

import dev.dimo.webflux.security.webfluxsecurity.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class WebfluxSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebfluxSecurityApplication.class, args);
	}

}
