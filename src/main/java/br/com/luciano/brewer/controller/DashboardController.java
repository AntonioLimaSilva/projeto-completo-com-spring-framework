package br.com.luciano.brewer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.luciano.brewer.repository.Clientes;
import br.com.luciano.brewer.repository.Vendas;

/**
 * 
 * @author Luciano Lima
 *
 */
@Controller
public class DashboardController {
	
	@Autowired
	private Vendas vendas;
	
	@Autowired
	private Clientes clientes;
	
	@GetMapping("/")
	public ModelAndView dashboard() {
		ModelAndView mv = new ModelAndView("Dashboard");
		mv.addObject("valorTotalNoAno", vendas.valorTotalNoAno());
		mv.addObject("valorTotalNoMes", vendas.valorTotalNoMes());
		mv.addObject("ticktMedioNoAno", vendas.ticketMedioNoAno());
		mv.addObject("totalClientes", clientes.count());
		mv.addObject("valorItensEstoque", vendas.valorItensEstoque());
		return mv;
	}

}
