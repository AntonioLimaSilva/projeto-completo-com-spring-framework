package br.com.luciano.brewer.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.StringUtils;

import br.com.luciano.brewer.validation.SKU;

@Entity
@Table(name = "cerveja")
public class Cerveja implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String sku;
	private String nome;
	private String descricao;
	private Origem origem;
	private Estilo estilo;
	private Sabor sabor;
	private String nomeFoto;
	private String contentType;
	private BigDecimal valor;
	private BigDecimal teorAlcoolico;
	private BigDecimal comissao;
	private Integer quantidadeEstoque;
	private boolean novaFoto;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@SKU
	@NotBlank(message = "SKU é obrigatório")
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@NotBlank(message = "Nome é obrigatório")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@NotBlank(message = "Descrição é obrigatória")
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@NotNull(message = "Origem é obrigatória")
	@Enumerated(EnumType.STRING)
	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	@NotNull(message = "Estilo é obrigatório")
	@ManyToOne
	@JoinColumn(name = "id_estilo")
	public Estilo getEstilo() {
		return estilo;
	}

	public void setEstilo(Estilo estilo) {
		this.estilo = estilo;
	}

	@NotNull(message = "Sabor é obrigatório")
	@Enumerated(EnumType.STRING)
	public Sabor getSabor() {
		return sabor;
	}

	public void setSabor(Sabor sabor) {
		this.sabor = sabor;
	}

	@Column(name = "nome_foto")
	public String getNomeFoto() {
		return nomeFoto;
	}

	public void setNomeFoto(String nomeFoto) {
		this.nomeFoto = nomeFoto;
	}

	@Column(name = "content_type")
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@NotNull(message = "Valor é obrigatório")
	@DecimalMin(value = "0.50", message = "O valor da cerveja deve ser maior que R$0.50")
	@DecimalMax(value = "9999999.99", message = "O valor da cerveja deve menor que R$9.999.999,99")
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@NotNull(message = "Teor alcoolico é obrigatório")
	@Column(name = "teor_alcoolico")
	public BigDecimal getTeorAlcoolico() {
		return teorAlcoolico;
	}

	public void setTeorAlcoolico(BigDecimal teorAlcoolico) {
		this.teorAlcoolico = teorAlcoolico;
	}

	@NotNull(message = "Comissão é obrigatória")
	public BigDecimal getComissao() {
		return comissao;
	}

	public void setComissao(BigDecimal comissao) {
		this.comissao = comissao;
	}

	@NotNull(message = "Quantidade estoque e obrigatório")
	@Max(value = 999, message = "Estoque deve ter no máximo 999")
	@Column(name = "quantidade_estoque")
	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}
	
	@Transient
	public boolean isNovaFoto() {
		return novaFoto;
	}

	public void setNovaFoto(boolean novaFoto) {
		this.novaFoto = novaFoto;
	}

	@PrePersist
	@PreUpdate
	public void salvarSkuCaixaAlta() {
		this.sku = this.sku.toUpperCase();
	}
	
	@Transient
	public String getFotoOuMock() {
		return !StringUtils.isEmpty(this.nomeFoto) ? this.nomeFoto : "cerveja-mock.png";
	}
	
	@Transient
	public boolean isNova() {
		return this.id == null;
	}
	
	@Transient
	public boolean temFoto() {
		return !StringUtils.isEmpty(nomeFoto);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cerveja other = (Cerveja) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
