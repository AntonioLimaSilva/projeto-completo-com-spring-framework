package br.com.luciano.brewer.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author Luciano Lima
 *
 */
@Controller
public class SegurancaController {

	@RequestMapping("/login")
	public String login(@AuthenticationPrincipal User user) {
		if(user != null) {
			return "redirect:/cervejas";
		}
		
		return "Login";
	}
	
	@RequestMapping("/403")
	public String acessoNegado() {
		return "403";
	}
	
}
