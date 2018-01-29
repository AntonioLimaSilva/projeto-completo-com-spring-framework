package br.com.luciano.brewer.service.event.cerveja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.luciano.brewer.storage.FotoStorage;

@Component
public class CervejaListener {
	
	@Autowired
	private FotoStorage fotoStorage;
	
	@EventListener(condition = "#evento.temFoto()")
	public void salvarFoto(CervejaEvent evento) {
		this.fotoStorage.salvarFoto(evento.getCerveja().getNomeFoto());
	}
}
