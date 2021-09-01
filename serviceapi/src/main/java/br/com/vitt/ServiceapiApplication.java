package br.com.vitt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EntityScan(basePackages = {"br.com.vitt.model"})
@ComponentScan(basePackages = {"br.*"})
@EnableJpaRepositories(basePackages = {"br.com.vitt.repository"})
@EnableTransactionManagement
@EnableWebMvc
@RestController
@EnableAutoConfiguration
@EnableCaching
public class ServiceapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceapiApplication.class, args);
		
		System.out.println(new BCryptPasswordEncoder().encode("123"));
	}

}
