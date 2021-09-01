package br.com.vitt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.vitt.model.Usuario;
import br.com.vitt.repository.UsuarioRepository;

@Service
public class ImplementacaoUserDetailService implements UserDetailsService{
	
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		/*Consultar no banco por usuario*/
		
		Usuario usuario = usuarioRepository.findUserByLogin(username);
		
		if(usuario == null) {
			throw new UsernameNotFoundException("Usuario não foi encontrado");
		}
		
		return new User(usuario.getLogin(), 
						usuario.getPassword(), 
						usuario.getAuthorities());
	}
	
	

}
