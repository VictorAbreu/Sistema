package br.com.vitt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.vitt.service.ImplementacaoUserDetailService;

//mapeia URL, endereços, autoriza ou bloqueia acesso a URL
@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private ImplementacaoUserDetailService implementacaoUserDetailService;
	
	/*Configura as solicitações de acesso por HTTP*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		/* Ativando a proteção contra usuarios que nao estão validados por token*/
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		
		/*Ativando a permissão para acesso a pagina inicial do sistema*/
		.disable().authorizeRequests().antMatchers("/").permitAll()
		.antMatchers("/index").permitAll()
		
		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		
		/*URL de logout - redireciona apos o user deslogar do sistema*/
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		
		/*mapeoa a url de logout e invalida o usuario*/
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		
		/*Filtra as requisições de login para autenticação*/
		.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
				 UsernamePasswordAuthenticationFilter.class)
		
		
		
		/*Filtra demais requisições para verificar a presença do TOKEN JWT no HEADER HTTP*/
		.addFilterBefore(new JWTAPIAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
		

	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		/*Service que irá consultar o usuario no banco de dados*/
		auth.userDetailsService(implementacaoUserDetailService)
		
		/*Padrão de codificação de senha*/
		.passwordEncoder(new BCryptPasswordEncoder());
		
	}

}
