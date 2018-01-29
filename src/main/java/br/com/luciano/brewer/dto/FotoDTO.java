package br.com.luciano.brewer.dto;

public class FotoDTO {

	private String nome;
	private String contentType;

	public FotoDTO(String nome, String contentType) {
		this.nome = nome;
		this.contentType = contentType;
	}

	public String getNome() {
		return nome;
	}

	public String getContentType() {
		return contentType;
	}

}
