package br.com.luciano.brewer.dto;

import java.math.BigDecimal;

public class ValorItensEstoque {

	private BigDecimal valor;
	private Long totalItens;

	public ValorItensEstoque(BigDecimal valor, Long totalItens) {
		this.valor = valor;
		this.totalItens = totalItens;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public Long getTotalItens() {
		return totalItens;
	}

}
