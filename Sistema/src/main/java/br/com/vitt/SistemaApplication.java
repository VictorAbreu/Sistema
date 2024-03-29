package br.com.vitt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = "br.com.vitt.model")
@ComponentScan({ "br.*" })
@EnableJpaRepositories(basePackages = { "br.com.vitt.repository" })
@EnableTransactionManagement
@EnableWebMvc
public class SistemaApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(SistemaApplication.class, args);

		/*
		 * BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); String result =
		 * encoder.encode("123"); System.out.println(result);
		 */
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {// redireciona para a tela de login

		registry.addViewController("/login").setViewName("/login");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);

	}

	/*@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
	}*/

}
