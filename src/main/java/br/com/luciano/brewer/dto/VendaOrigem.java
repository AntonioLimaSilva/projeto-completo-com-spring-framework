package br.com.luciano.brewer.dto;

import org.springframework.util.StringUtils;

public class VendaOrigem {

	private String mes;
	private Integer totalNacional;
	private Integer totalInternacional;

	public VendaOrigem(String mes, Integer totalNacional, Integer totalInternacional) {
		this.mes = mes;
		this.totalNacional = StringUtils.isEmpty(totalNacional) ? Integer.valueOf(0) : totalNacional;
		this.totalInternacional = StringUtils.isEmpty(totalInternacional) ? Integer.valueOf(0) : totalInternacional;
	}

	public String getMes() {
		return mes;
	}

	public Integer getTotalNacional() {
		return totalNacional;
	}

	public Integer getTotalInternacional() {
		return totalInternacional;
	}

}
