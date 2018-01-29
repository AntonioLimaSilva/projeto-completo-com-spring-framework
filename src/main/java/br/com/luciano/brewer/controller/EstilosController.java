package br.com.luciano.brewer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.luciano.brewer.controller.page.PageWrapper;
import br.com.luciano.brewer.model.Estilo;
import br.com.luciano.brewer.repository.Estilos;
import br.com.luciano.brewer.repository.filter.EstiloFilter;
import br.com.luciano.brewer.service.CadastroEstiloService;
import br.com.luciano.brewer.service.exception.NegocioException;

/**
 * 
 * @author Luciano Lima
 *
 */
@Controller
@RequestMapping("/estilos")
public class EstilosController {
	
	@Autowired
	private CadastroEstiloService cadastro;
	
	@Autowired
	private Estilos estilos;

	@RequestMapping("/novo")
	public ModelAndView novo(Estilo estilo) {
		ModelAndView mv = new ModelAndView("estilo/CadastroEstilo");
		return mv;
	}
	
	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Estilo estilo, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			return novo(estilo);
		}
		
		try {
			this.cadastro.salvar(estilo);
		} catch(NegocioException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(estilo);
		}
		attributes.addFlashAttribute("mensagem", "Estilo salvo com sucesso!");
		return new ModelAndView("redirect:/estilos/novo");
	}
	
	@GetMapping("/{id}")
	public ModelAndView editar(@PathVariable Integer id) {
		Estilo estilo = this.estilos.findOne(id);
		ModelAndView mv = novo(estilo);
		mv.addObject(estilo);
		
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
	
	@GetMapping
	public ModelAndView pesquisar(EstiloFilter estiloFilter, @PageableDefault(size = 2) Pageable pageable,
			HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("estilo/PesquisaEstilos");
		PageWrapper<Estilo> paginaWrapper = new PageWrapper<>(estilos.filtrar(estiloFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ResponseEntity<?> salvarEstiloRapido(@RequestBody @Valid Estilo estilo, BindingResult result) {
		if(result.hasErrors()) {
			return ResponseEntity.badRequest().body(result.getFieldError("nome").getDefaultMessage());
		}	
		estilo = this.cadastro.salvar(estilo);		
		return ResponseEntity.ok(estilo);
	}
	
}
