package br.com.vitt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebConfigSecurity extends WebSecurityConfigurerAdapter{


	 /* public void addResourceHandlers(ResourceHandlerRegistry registry) {
	 registry.addResourceHandler("/webjars/**", "/resources/**", "/static/**", "/img/**", "/css/**", "/js/**",
					"classpath:/static/", "classpath:/resources/")
			.addResourceLocations("/webjars/", "/resources/",
							"classpath:/static/**", "classpath:/static/img/**", "classpath:/static/",
							"classpath:/resources/", "classpath:/static/css/", "classpath:/static/js/", "/resources/**",
							"/WEB-INF/classes/static/**");
			
	  }*/
	 
	
	@Autowired
	private ImplementacaoUserDetailService implementacaoUserDetailService;
	
	@Override // Confira as solicitações de acesso por http
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf()
		.disable() // Desativa as configurações padrão de memória.
		.authorizeRequests() // Pertimi restringir acessos
		.antMatchers(HttpMethod.GET, "/").permitAll() // Qualquer usuário acessa a pagina inicial
		//.antMatchers(HttpMethod.GET, "/cadastrocliente").hasAnyRole("ADMIN")
		.anyRequest().authenticated()
		.and().formLogin().permitAll()// permite qualquer usuário
        .loginPage("/login")
        .defaultSuccessUrl("/cadastrocliente")
        .failureUrl("/login?error=true")
        .and()
        .logout().logoutSuccessUrl("/login")
		// Mapeia URL de Logout e invalida usuário autenticado
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
		
	}
	
	@Override // Cria autenticação do usuario com banco de dados ou em memória
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(implementacaoUserDetailService)
		.passwordEncoder(new BCryptPasswordEncoder());
		
		//auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance()) --> Não estamos criptografando a senha
		/*auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())//criptografando a senha
		.withUser("victor")
		.password("$2a$10$D3.D7Z3qDE9E1jEOkGTfVu1ZVZ5uTLSJMZ0jSXY6xU3S1GLeCPUmS")
		.roles("ADMIN");*/
		
	}
	
	
	@Override//Ignora URL específicas. Ex. pags do CSS
	public void configure(WebSecurity web) throws Exception {
		
		web.ignoring().antMatchers("/materialize/**")/*
		.antMatchers(HttpMethod.GET,"/resources/**","/static/**", "/**", "/materialize/**")*/;
		
	}

}
