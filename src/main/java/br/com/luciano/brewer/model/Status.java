package br.com.luciano.brewer.model;

public enum Status {

	ORCAMENTO("Orçamento"),
	EMITIDO("Emitido"),
	CANCELADO("Cancelado");
	
	private String descricao;
	
	Status(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
