package br.com.luciano.brewer.service.event.cerveja;

import org.springframework.util.StringUtils;

import br.com.luciano.brewer.model.Cerveja;

public class CervejaEvent {

	private Cerveja cerveja;

	public CervejaEvent(Cerveja cerveja) {
		this.cerveja = cerveja;		
	}

	public Cerveja getCerveja() {
		return cerveja;
	}
	
	public boolean temFoto() {
		return !StringUtils.isEmpty(cerveja.getNomeFoto());
	}
}
