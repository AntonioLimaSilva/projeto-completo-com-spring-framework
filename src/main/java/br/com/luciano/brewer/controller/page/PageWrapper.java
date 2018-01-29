package br.com.luciano.brewer.controller.page;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.util.UriComponentsBuilder;

public class PageWrapper<T> {

	private Page<T> page;
	private UriComponentsBuilder uriBuilder;

	public PageWrapper(Page<T> page, HttpServletRequest httpServletRequest) {
		this.page = page;	
		String httpUrl = httpServletRequest.getRequestURL().append(
				httpServletRequest.getQueryString() != null ? "?" + httpServletRequest.getQueryString() : "")
				.toString().replaceAll("\\+", "%20").replaceAll("excluido", "");
		this.uriBuilder = UriComponentsBuilder.fromHttpUrl(httpUrl);
	}
	
	public List<T> getConteudo() {
		return this.page.getContent();
	}
	
	public boolean isPrimeira() {
		return this.page.isFirst();
	}
	
	public boolean isUltima() {
		return this.page.isLast();
	}
	
	public int getAtual() {
		return this.page.getNumber();
	}
	
	public int getTotal() {
		return this.page.getTotalPages();
	}
	
	public boolean isVazia() {
		return this.page.getContent().isEmpty();
	}
	
	public String urlParaPagina(int numeroPagina) {
		return this.uriBuilder.replaceQueryParam("page", numeroPagina).build(true).encode().toUriString();
	}
	
	public String urlParaOrdenar(String propriedade) {
		UriComponentsBuilder uriBuilderOrder = UriComponentsBuilder.fromUriString(uriBuilder.build(true).encode().toUriString());	
		String ValorOrder = String.format("%s,%s", propriedade, inverterDirecao(propriedade));		
		return uriBuilderOrder.replaceQueryParam("sort", ValorOrder).build(true).encode().toUriString();
	}
	
	public boolean descendente(String propriedade) {
		return inverterDirecao(propriedade).equals("asc");
	}
	
	public boolean ordenada(String propriedade) {
		Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null;
		
		if(order == null) {
			return false;
		}
		
		return page.getSort().getOrderFor(propriedade) != null ? true : false;
	}
	
	private String inverterDirecao(String propriedade) {
		String direcao = "asc";
		Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null;
		if(order != null) {
			direcao = Sort.Direction.ASC.equals(order.getDirection()) ? "desc" : "asc";
		}
		return direcao;
	}
}
