package br.com.luciano.brewer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.luciano.brewer.controller.page.PageWrapper;
import br.com.luciano.brewer.model.Usuario;
import br.com.luciano.brewer.repository.Grupos;
import br.com.luciano.brewer.repository.Usuarios;
import br.com.luciano.brewer.repository.filter.UsuarioFilter;
import br.com.luciano.brewer.service.CadastroUsuarioService;
import br.com.luciano.brewer.service.StatusUsuario;
import br.com.luciano.brewer.service.exception.IlegalEmailException;
import br.com.luciano.brewer.service.exception.IlegalSenhaException;
import br.com.luciano.brewer.service.exception.NegocioException;

/**
 * 
 * @author Luciano Lima
 *
 */
@Controller
@RequestMapping("/usuarios")
public class UsuariosController {
	
	@Autowired
	private CadastroUsuarioService cadastro;
	
	@Autowired
	private Grupos grupos;
	
	@Autowired
	private Usuarios usuarios;

	@RequestMapping("/novo")
	public ModelAndView novo(Usuario usuario) {
		ModelAndView mv = new ModelAndView("usuario/CadastroUsuario");
		mv.addObject("grupos", this.grupos.findAll());
		return mv;
	}
	
	@RequestMapping(value = { "/novo", "{\\+d}" }, method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			return novo(usuario);
		}
		
		try {
			this.cadastro.salvar(usuario);
		} catch(IlegalEmailException e) {
			result.rejectValue("email", e.getMessage(), e.getMessage());
			return novo(usuario);
		} catch(IlegalSenhaException e) {
			result.rejectValue("senha", e.getMessage(), e.getMessage());
			return novo(usuario);
		}
		
		attributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso");
		return new ModelAndView("redirect:/usuarios/novo");
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
	
	@GetMapping
	public ModelAndView pesquisar(UsuarioFilter usuarioFilter, @PageableDefault(size = 2) Pageable pageable,
			HttpServletRequest httpServletRequest) {		
		ModelAndView mv = new ModelAndView("usuario/PesquisaUsuarios");	
		mv.addObject("grupos", this.grupos.findAll());
		
		PageWrapper<Usuario> pagina = new PageWrapper<>(this.usuarios.filtrar(usuarioFilter, pageable), httpServletRequest);	
		mv.addObject("pagina", pagina);
		
		return mv;
	}
	
	@GetMapping("/{id}")
	public ModelAndView editar(@PathVariable Integer id) {
		Usuario usuario = this.usuarios.buscarComGrupos(id);
		ModelAndView mv = novo(usuario);
		mv.addObject(usuario);
		return mv;
	}
	
	@PutMapping("/status")
	@ResponseStatus(HttpStatus.OK) // É preciso por que não esta retornando uma view
	public void atualizarStatus(@RequestParam("ids[]") Integer[] ids, @RequestParam("status") StatusUsuario statusUsuario) {
		this.cadastro.alterarStatus(ids, statusUsuario);
	}
	
}
