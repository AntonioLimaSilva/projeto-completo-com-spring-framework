package br.com.luciano.brewer.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.luciano.brewer.controller.page.PageWrapper;
import br.com.luciano.brewer.controller.validator.VendaValidator;
import br.com.luciano.brewer.dto.VendaMes;
import br.com.luciano.brewer.dto.VendaOrigem;
import br.com.luciano.brewer.mail.Mailer;
import br.com.luciano.brewer.model.Cerveja;
import br.com.luciano.brewer.model.Status;
import br.com.luciano.brewer.model.Venda;
import br.com.luciano.brewer.repository.Cervejas;
import br.com.luciano.brewer.repository.Vendas;
import br.com.luciano.brewer.repository.filter.VendaFilter;
import br.com.luciano.brewer.security.UsuarioSistema;
import br.com.luciano.brewer.service.CadastroVendaService;
import br.com.luciano.brewer.service.RelatorioVendaService;
import br.com.luciano.brewer.service.exception.NegocioException;
import br.com.luciano.brewer.session.TabelaItensSession;

/**
 * 
 * @author Luciano Lima
 *
 */
@Controller
@RequestMapping("/vendas")
public class VendasController {
	
	@Autowired
	private RelatorioVendaService relatorioVendaService;
	
	@Autowired
	private Mailer mailer;
	
	@Autowired
	private Cervejas cervejas;
	
	@Autowired
	private CadastroVendaService cadastro;
	
	@Autowired
	private TabelaItensSession tabelaItens;
	
	@Autowired
	private Vendas vendas;
	
	@Autowired
	private VendaValidator vendaValidator;
	
	@InitBinder("venda")
	public void inicializarValidator(WebDataBinder binder) {
		binder.setValidator(vendaValidator);
	}
	
	@RequestMapping("/nova")
	public ModelAndView nova(Venda venda) {
		ModelAndView mv = new ModelAndView("venda/CadastroVenda");
		
		setUuid(venda);
		
		mv.addObject("itens", this.tabelaItens.getItens(venda.getUuid()));
		mv.addObject("valorTotalItens", this.tabelaItens.getValorTotal(venda.getUuid()));
		mv.addObject("valorFrete", venda.getValorFrete());
		mv.addObject("valorDesconto", venda.getValorDesconto());
		
		return mv;	
	}

	
	@RequestMapping(value = "/nova", method = RequestMethod.POST, params = "salvar")
	public ModelAndView salvar(Venda venda, BindingResult result, RedirectAttributes attributes, 
			@AuthenticationPrincipal UsuarioSistema usuarioSistema) {		
		validarVenda(venda, result);
		if(result.hasErrors()) {
			return nova(venda);
		}
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		this.cadastro.salvar(venda);
		attributes.addFlashAttribute("mensagem", "Venda salva com sucesso");
		return new ModelAndView("redirect:/vendas/nova");
	}

	@RequestMapping(value = "/nova", method = RequestMethod.POST, params = "emitir")
	public ModelAndView emitir(Venda venda, BindingResult result, RedirectAttributes attributes, 
			@AuthenticationPrincipal UsuarioSistema usuarioSistema) {		
		validarVenda(venda, result);
		if(result.hasErrors()) {
			return nova(venda);
		}
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		this.cadastro.emitir(venda);
		attributes.addFlashAttribute("mensagem", "Venda emitida com sucesso");
		return new ModelAndView("redirect:/vendas/nova");
	}
	
	@RequestMapping(value = "/nova", method = RequestMethod.POST, params = "enviarEmail")
	public ModelAndView enviarEmail(Venda venda, BindingResult result, RedirectAttributes attributes, 
			@AuthenticationPrincipal UsuarioSistema usuarioSistema) {		
		validarVenda(venda, result);
		if(result.hasErrors()) {
			return nova(venda);
		}
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		this.cadastro.emitir(venda);
		this.mailer.enviar(venda);
		
		attributes.addFlashAttribute("mensagem", "Venda enviada por email com sucesso");
		return new ModelAndView("redirect:/vendas/nova");
	}
	
	@RequestMapping(value = "/nova", method = RequestMethod.POST, params = "cancelar")
	public ModelAndView cancelar(Venda venda, BindingResult result, RedirectAttributes attributes, 
			@AuthenticationPrincipal UsuarioSistema usuarioSistema) {		
		validarVenda(venda, result);
		if(result.hasErrors()) {
			return nova(venda);
		}
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		this.cadastro.cancelar(venda);
		attributes.addFlashAttribute("mensagem", "Venda cancelada com sucesso");
		return new ModelAndView("redirect:/vendas/" + venda.getId());
	}
	
	@GetMapping
	public ModelAndView pesquisar(VendaFilter vendaFilter, @PageableDefault(size = 3) Pageable pageable,
			HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("venda/PesquisaVendas");
		PageWrapper<Venda> pagina = new PageWrapper<>(this.vendas.filtrar(vendaFilter, pageable), httpServletRequest);
		mv.addObject("status", Status.values());
		mv.addObject("pagina", pagina);
		return mv;
	}
	
	@GetMapping("/{id}")
	public ModelAndView editar(@PathVariable Integer id) {
		Venda venda = this.vendas.buscarComItens(id);
		setUuid(venda);
		
		venda.getItens().stream().forEach( item ->  {
			this.tabelaItens.adicionar(venda.getUuid(), item.getCerveja(), item.getQuantidade());
		});
		
		ModelAndView mv = nova(venda);
		mv.addObject(venda);
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
	
	@GetMapping("/relatorio/{id}")
	public ResponseEntity<byte[]> gerarRelatorio(@PathVariable Integer id) throws Exception {
		byte[] relatorio = relatorioVendaService.gerarRelatorioVendaComItens(id);	
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(relatorio);
	}
	
	@PostMapping("/item")
	public ModelAndView adicionarItem(String uuid, Integer idCerveja) {	
		Cerveja cerveja = this.cervejas.findOne(idCerveja);
		this.tabelaItens.adicionar(uuid, cerveja, 1);
				
		return tabelaItensVenda(uuid);
	}
	
	@PutMapping("/item/{idCerveja}")
	public ModelAndView alterarQuantidadeItem(@PathVariable Integer idCerveja, String uuid, Integer quantidade) {
		Cerveja cerveja = this.cervejas.findOne(idCerveja);
		this.tabelaItens.alterarQuantidade(uuid, cerveja, quantidade);
		
		return tabelaItensVenda(uuid);
	}
	
	@DeleteMapping("/item/{uuid}/{idCerveja}")
	public ModelAndView excluirItem(@PathVariable String uuid, @PathVariable Integer idCerveja) {
		Cerveja cerveja = this.cervejas.findOne(idCerveja);
		this.tabelaItens.excluir(uuid, cerveja);
		
		return tabelaItensVenda(uuid);
	}
	
	@GetMapping("/totalPorMes")
	public @ResponseBody List<VendaMes> totalPorMes() {
		return this.vendas.totalPorMes();
	}
	
	@GetMapping("/totalPorOrigem")
	public @ResponseBody List<VendaOrigem> totalPorOrigem() {
		return this.vendas.totalPorOrigem();
	}
	
	private ModelAndView tabelaItensVenda(String uuid) {
		ModelAndView mv = new ModelAndView("venda/TabelaItensVenda");
		mv.addObject("itens", this.tabelaItens.getItens(uuid));
		mv.addObject("valorTotal", this.tabelaItens.getValorTotal(uuid));
		return mv;
	}
	
	private void setUuid(Venda venda) {
		if(venda.getUuid() == null) {
			venda.setUuid(UUID.randomUUID().toString());
		}
	}
	
	private void validarVenda(Venda venda, BindingResult result) {
		venda.adicionarItens(tabelaItens.getItens(venda.getUuid()));
		venda.calcularValorTotal();
		
		vendaValidator.validate(venda, result);
	}

}
