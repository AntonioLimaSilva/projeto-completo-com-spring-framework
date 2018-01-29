package br.com.luciano.brewer.dto;

import java.math.BigDecimal;

import org.springframework.util.StringUtils;

import br.com.luciano.brewer.model.Origem;

public class CervejaDTO {
	
	private Integer id;
	private String sku;
	private String nome;
	private String origem;
	private BigDecimal valor;
	private String nomeFoto;
	
	public CervejaDTO(Integer id, String sku, String nome, Origem origem, BigDecimal valor, String nomeFoto) {
		this.id = id;
		this.sku = sku;
		this.nome = nome;
		this.origem = origem.getDescricao();
		this.valor = valor;
		this.nomeFoto = !StringUtils.isEmpty(nomeFoto) ? nomeFoto : "cerveja-mock.png";
	}

	public Integer getId() {
		return id;
	}

	public String getSku() {
		return sku;
	}

	public String getNome() {
		return nome;
	}

	public String getOrigem() {
		return origem;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public String getNomeFoto() {
		return nomeFoto;
	}

}
