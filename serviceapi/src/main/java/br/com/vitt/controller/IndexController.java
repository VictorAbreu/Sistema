package br.com.vitt.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.vitt.model.Usuario;
import br.com.vitt.model.UsuarioDTO;
import br.com.vitt.repository.UsuarioRepository;

@CrossOrigin
@RestController /* Arquitetura rest */
@RequestMapping(value = "/usuario")
public class IndexController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	/* Serviço Restful */
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<UsuarioDTO> consultaUsuariosId(@PathVariable(value = "id") Long id) {

		Optional<Usuario> usuario = usuarioRepository.findById(id);

		return new ResponseEntity<UsuarioDTO>(new UsuarioDTO(usuario.get()), HttpStatus.OK);

	}
	
	/* Serviço Restful */
	@GetMapping(value = "/uc/{id}", produces = "application/json")
	public ResponseEntity<Usuario> consultaUsuariosIduc(@PathVariable(value = "id") Long id) {

		Optional<Usuario> usuario = usuarioRepository.findById(id);

		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);

	}

	/* Serviço Restful */
	/*Supor que o carregamento seja um processo lento e queremos controlar ele com cache*/
	@GetMapping(value = "/", produces = "application/json")
	@CacheEvict(value="cacheuser", allEntries = true)
	@CachePut("cacheuser")
	public ResponseEntity<List<Usuario>> consultausuariUsuarios() {

		List<Usuario> usu = (List<Usuario>) usuarioRepository.findAll();

		return new ResponseEntity<List<Usuario>>(usu, HttpStatus.OK);

	}
	
	
	@GetMapping(value = "/usuariopornome/{nome}", produces = "application/json")
	public ResponseEntity<List<Usuario>> consultaUsuarioPorNome(@PathVariable("nome") String nome) {

		List<Usuario> usu = (List<Usuario>) usuarioRepository.findUserByNome(nome);

		return new ResponseEntity<List<Usuario>>(usu, HttpStatus.OK);

	}

	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {

		String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());

		usuario.setSenha(senhaCriptografada);

		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);

	}

	@DeleteMapping(value = "/{id}", produces = "application/text")
	public String deletevenda(@PathVariable("id") Long id) {

		usuarioRepository.deleteById(id);

		return "ok";
	}

	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario) {
		
		System.out.println(usuario.getId());

		//Usuario userTemporario = usuarioRepository.findUserByLogin(usuario.getLogin());//quando necessario mudar para buscar por id
		Usuario userTemporario = usuarioRepository.findById(usuario.getId()).get();//quando necessario mudar para buscar por id

		if (!userTemporario.getSenha().equals(usuario.getSenha())) { /* Senhas diferentes */
			String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhacriptografada);
		}

		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);

	}

}
