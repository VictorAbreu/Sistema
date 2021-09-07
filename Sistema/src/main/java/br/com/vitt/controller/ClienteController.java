package br.com.vitt.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.vitt.model.Cliente;
import br.com.vitt.model.Pagamento;
import br.com.vitt.repository.ClienteRepository;
import br.com.vitt.repository.PagamentoRepository;

@Controller
public class ClienteController {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;

	@RequestMapping(method = RequestMethod.GET, value = "cadastrocliente")
	public ModelAndView inicio() {

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastrocliente");

		modelAndView.addObject("clienteobj", new Cliente());
		
		Iterable<Cliente> clienteIt = clienteRepository.findAll();

		modelAndView.addObject("clientes", clienteIt);

		return modelAndView;

	}

	@RequestMapping(method = RequestMethod.POST, value = "**/salvarcliente")
	public ModelAndView salvar(@Valid Cliente cliente, BindingResult bindingResult) {//@Valid e BindingResult retornam a mensagem de erro sobre validação

		cliente.setPagamento(pagamentoRepository.getPagamentos(cliente.getId()));
		
		if(bindingResult.hasErrors()) {/*Se houver erros de validação entra nessa condição*/
			
			ModelAndView andView = new ModelAndView("cadastro/cadastrocliente");
			Iterable<Cliente> clienteIt = clienteRepository.findAll();

			andView.addObject("clientes", clienteIt);
			andView.addObject("clienteobj", cliente);
			
			
			List<String> msg = new ArrayList<String>();
			for(ObjectError objectError : bindingResult.getAllErrors()) {
				
				msg.add(objectError.getDefaultMessage());//getDefaultMessage() retorna as mensagens informadas nas validações(anotções) nos models
				
			}
			
			andView.addObject("msg", msg);
			
			
			return andView;
			
		}
		
		
		clienteRepository.save(cliente);

		ModelAndView andView = new ModelAndView("cadastro/cadastrocliente");

		Iterable<Cliente> clienteIt = clienteRepository.findAll();

		andView.addObject("clientes", clienteIt);
		andView.addObject("clienteobj", new Cliente());

		return andView;

	}

	@RequestMapping(method = RequestMethod.GET, value = "listacliente")
	public ModelAndView clientes() {

		ModelAndView andView = new ModelAndView("cadastro/cadastrocliente");

		Iterable<Cliente> clienteIt = clienteRepository.findAll();

		andView.addObject("clientes", clienteIt);
		andView.addObject("clienteobj", new Cliente());

		return andView;

	}

	@GetMapping("/editarcliente/{idcliente}")
	public ModelAndView editarCliente(@PathVariable("idcliente") Long idcliente) {

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastrocliente");

		Optional<Cliente> cliente = clienteRepository.findById(idcliente);

		modelAndView.addObject("clienteobj", cliente.get());

		return modelAndView;

	}
	
	@GetMapping("/excluircliente/{idcliente}")
	public ModelAndView excluirCliente(@PathVariable("idcliente") Long idcliente) {

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastrocliente");

		clienteRepository.deleteById(idcliente);

		modelAndView.addObject("clienteobj", new Cliente());
		
		Iterable<Cliente> clienteIt = clienteRepository.findAll();

		modelAndView.addObject("clientes", clienteIt);

		return modelAndView;

	}
	
	@PostMapping("**/pesquisarcliente")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastrocliente");
		modelAndView.addObject("clientes", clienteRepository.findClienteByName(nomepesquisa));
		modelAndView.addObject("clienteobj", new Cliente());
		
		return modelAndView;
		
	}
	
	@GetMapping("/pagamentocliente/{idcliente}")
	public ModelAndView pagamentoCliente(@PathVariable("idcliente") Long idcliente) {

		ModelAndView modelAndView = new ModelAndView("cadastro/pagamentocliente");

		Optional<Cliente> cliente = clienteRepository.findById(idcliente);

		modelAndView.addObject("clienteobj", cliente.get());
		modelAndView.addObject("pagamentos", pagamentoRepository.getPagamentos(idcliente));

		return modelAndView;

	}
	
	
	@PostMapping("**/pagamentocliente/{clienteid}")
	public ModelAndView pagamentoCliente(Pagamento pagamento, 
										@PathVariable("clienteid") Long clienteid) {
		
		Cliente cliente = clienteRepository.findById(clienteid).get();
		pagamento.setCliente(cliente);
		
		pagamentoRepository.save(pagamento);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/pagamentocliente");
		
		modelAndView.addObject("clienteobj", cliente);
		modelAndView.addObject("pagamentos", pagamentoRepository.getPagamentos(clienteid));
		
		return modelAndView;
		
	}
	
	@GetMapping("/excluirpagamento/{idpagamento}")
	public ModelAndView excluirPagamento(@PathVariable("idpagamento") Long idpagamento) {

		Cliente cliente = pagamentoRepository.findById(idpagamento).get().getCliente();
		
		pagamentoRepository.deleteById(idpagamento);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/pagamentocliente");

		modelAndView.addObject("clienteobj", cliente);
		modelAndView.addObject("pagamentos", pagamentoRepository.getPagamentos(cliente.getId()));

		return modelAndView;

	}

}
