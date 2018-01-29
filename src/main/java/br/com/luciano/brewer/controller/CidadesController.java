package br.com.luciano.brewer.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.luciano.brewer.controller.page.PageWrapper;
import br.com.luciano.brewer.model.Cidade;
import br.com.luciano.brewer.repository.Cidades;
import br.com.luciano.brewer.repository.Estados;
import br.com.luciano.brewer.repository.filter.CidadeFilter;
import br.com.luciano.brewer.service.CadastroCidadeService;
import br.com.luciano.brewer.service.exception.NegocioException;

/**
 * 
 * @author Luciano Lima
 *
 */
@Controller
@RequestMapping("/cidades")
public class CidadesController {
	
	@Autowired
	private Cidades cidades;
	
	@Autowired
	private CadastroCidadeService cadastro;
	
	@Autowired
	private Estados estados;

	@RequestMapping("/nova")
	public ModelAndView nova(Cidade cidade) {
		ModelAndView mv = new ModelAndView("cidade/CadastroCidade");
		mv.addObject("estados", this.estados.findAll());
		return mv;
	}
	
	/**
	 * Obs: O método chamado em cidade do condicion do CacheEvict segue o padrão java bean, mas poderia ser por exemplo:
	 * | condition = "#cidade.temEstado()"  | dessa forma teria que adicionar o nome do método todo e finalizar com abre e 
	 * fecha parenteses
	 * @param cidade
	 * @param result
	 * @param attributes
	 * @return
	 */
	@CacheEvict(value = "cidades", key = "#cidade.estado.id", condition = "#cidade.estadoExistente")
	@RequestMapping(value = "/nova", method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Cidade cidade, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			return nova(cidade);
		}
		
		try {
			this.cadastro.salvar(cidade);
		} catch(NegocioException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return nova(cidade);
		}
		
		return new ModelAndView("redirect:/cidades/nova");
	}
	
	@GetMapping
	public ModelAndView pesquisar(CidadeFilter cidadeFilter, @PageableDefault(size = 2) Pageable pageable
			, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("cidade/PesquisaCidades");
		PageWrapper<Cidade> pagina = new PageWrapper<>(this.cidades.filtrar(cidadeFilter, pageable), httpServletRequest);
		mv.addObject("estados", this.estados.findAll());
		mv.addObject("pagina", pagina);
		return mv;
	}
	
	@GetMapping("/{id}")
	public ModelAndView editar(@PathVariable Integer id) {
		Cidade cidade = this.cidades.buscarComEstado(id);
		ModelAndView mv = nova(cidade);
		mv.addObject(cidade);
		
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
	
	@Cacheable(value = "cidades", key = "#idEstado")
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody List<Cidade> buscarCidade(@RequestParam(name = "estado", defaultValue = "-1") Integer idEstado) throws InterruptedException {
		
		Thread.sleep(1000);
		
		return this.cidades.findByEstadoId(idEstado);
	}
	
}
