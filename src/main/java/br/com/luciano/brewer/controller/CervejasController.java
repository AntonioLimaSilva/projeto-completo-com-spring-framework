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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.luciano.brewer.controller.page.PageWrapper;
import br.com.luciano.brewer.dto.CervejaDTO;
import br.com.luciano.brewer.model.Cerveja;
import br.com.luciano.brewer.model.Origem;
import br.com.luciano.brewer.model.Sabor;
import br.com.luciano.brewer.repository.Cervejas;
import br.com.luciano.brewer.repository.Estilos;
import br.com.luciano.brewer.repository.filter.CervejaFilter;
import br.com.luciano.brewer.service.CadastroCervejaService;
import br.com.luciano.brewer.service.exception.NegocioException;

/**
 * 
 * @author Luciano Lima
 *
 */

@Controller
@RequestMapping("/cervejas")
public class CervejasController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(Cerveja.class);
	
	@Autowired
	private Estilos estilos;
	
	@Autowired
	private CadastroCervejaService cadastro;

	@Autowired
	private Cervejas cervejas;
	
	@RequestMapping("/nova") 
	public ModelAndView nova(Cerveja cerveja) {
		ModelAndView mv = new ModelAndView("cerveja/CadastroCerveja");
		mv.addObject("estilos", estilos.findAll());
		mv.addObject("sabores", Sabor.values());
		mv.addObject("origens", Origem.values());
		return mv;		
	}
	
	/**
	 * Método configurado para receber dois tipos de requisição, uma para "cervejas/nova" e outra para "cervejas/1"
	 * atravez de uma expressão regular é possível realizar tanto o cadastro de uma cerveja como realizar a edição
	 * @param cerveja
	 * @param result
	 * @param attributes
	 * @return ModelAndView
	 */
	@RequestMapping(value = { "/nova", "{\\+d}" }, method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Cerveja cerveja, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			return nova(cerveja);
		}
		
		this.cadastro.salvar(cerveja);
		
		attributes.addFlashAttribute("mensagem", "Cadastro salvo com sucesso!");
		return new ModelAndView("redirect:/cervejas/nova");
	}
	
	@GetMapping
	public ModelAndView pesquisar(CervejaFilter cervejaFilter, BindingResult result, @PageableDefault(size = 2) Pageable pageable,
			HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("cerveja/PesquisaCervejas");
		mv.addObject("estilos", this.estilos.findAll());
		mv.addObject("sabores", Sabor.values());
		mv.addObject("origens", Origem.values());
		PageWrapper<Cerveja> paginaWrapper = new PageWrapper<>(this.cervejas.filtrar(cervejaFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	@GetMapping("/{id}")
	public ModelAndView editar(@PathVariable Integer id) {
		Cerveja cerveja = this.cervejas.findOne(id);
		
		ModelAndView mv = nova(cerveja);
		mv.addObject(cerveja);
		return mv;
	}
	
	@DeleteMapping("/{id}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("id") Integer idCerveja) {
		Cerveja cerveja = this.cervejas.findOne(idCerveja);
		try {
			this.cadastro.excluir(cerveja);
		} catch (NegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<CervejaDTO> pesquisar(String skuOuNome) {
		return this.cervejas.porSkuOuNome(skuOuNome);
	}

}
