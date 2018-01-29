package br.com.luciano.brewer.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.luciano.brewer.controller.page.PageWrapper;
import br.com.luciano.brewer.model.Cliente;
import br.com.luciano.brewer.model.TipoPessoa;
import br.com.luciano.brewer.repository.Clientes;
import br.com.luciano.brewer.repository.Estados;
import br.com.luciano.brewer.repository.filter.ClienteFilter;
import br.com.luciano.brewer.service.CadastroClienteService;
import br.com.luciano.brewer.service.exception.NegocioException;

/**
 * 
 * @author Luciano Lima
 *
 */
@Controller
@RequestMapping("/clientes")
public class ClientesController {
	
	@Autowired
	private Estados estados;
	
	@Autowired
	private Clientes clientes;
	
	@Autowired
	private CadastroClienteService cadastro;
	
	@RequestMapping("/novo")
	public ModelAndView novo(Cliente cliente) {
		ModelAndView mv = new ModelAndView("cliente/CadastroCliente");
		mv.addObject("tiposPessoa", TipoPessoa.values());
		mv.addObject("estados", this.estados.findAll());
		return mv;
	}
	
	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Cliente cliente, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			return novo(cliente);
		}
		
		try {
			this.cadastro.salvar(cliente);
		} catch(NegocioException e) {
			result.rejectValue("cpfOuCnpj", e.getMessage(), e.getMessage());
			return novo(cliente);
		}
		
		attributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso!");
		
		return new ModelAndView("redirect:/clientes/novo");
	}
	
	@GetMapping
	public ModelAndView pesquisar(ClienteFilter clienteFilter, @PageableDefault(size = 2) Pageable pageable,
			HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("cliente/PesquisaClientes");
		
		PageWrapper<Cliente> pagina = new PageWrapper<>(clientes.filtar(clienteFilter, pageable), httpServletRequest);
 		
		mv.addObject("pagina", pagina);
		
		return mv;
	}
	
	@GetMapping("/{id}")
	public ModelAndView editar(@PathVariable Integer id) {
		Cliente cliente = this.clientes.findOne(id);
		ModelAndView mv = novo(cliente);
		mv.addObject(cliente);
		return mv;
	}
	
	@DeleteMapping("/{id}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable Integer id) {
		try {
			this.cadastro.excluir(id);
		} catch(NegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody List<Cliente> pesquisar(String nome) {
		this.validarTamanhoNome(nome);
		return this.clientes.findByNomeStartingWithIgnoreCase(nome);
	}
	
	/**
	 * Aqui é criado um tratador de excesão do tipo IllegalArgumentException para esse controller, então todo tipo
	 * de IllegalArgumentException para esse controller será capturada por esse método
	 * @param illegalArgumentException
	 * @return IllegalArgumentException
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Void> tratarIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.badRequest().build();
	}
	
	private void validarTamanhoNome(String nome) {
		if(StringUtils.isEmpty(nome) || nome.length() < 3)
			throw new IllegalArgumentException();
	}
}
