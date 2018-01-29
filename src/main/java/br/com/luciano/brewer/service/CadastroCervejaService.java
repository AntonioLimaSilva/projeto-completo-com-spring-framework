package br.com.luciano.brewer.service;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.luciano.brewer.model.Cerveja;
import br.com.luciano.brewer.repository.Cervejas;
import br.com.luciano.brewer.service.event.cerveja.CervejaEvent;
import br.com.luciano.brewer.service.exception.NegocioException;

@Service
public class CadastroCervejaService {
	
	@Autowired
	private Cervejas cervejas;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Transactional
	public void salvar(Cerveja cerveja) {
		this.cervejas.save(cerveja);
		//TODO
		publisher.publishEvent(new CervejaEvent(cerveja));
	}

	@Transactional
	public void excluir(Cerveja cerveja) {
		try {
			this.cervejas.delete(cerveja);
			this.cervejas.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Erro excluíndo cerveja");
		}
	}

}
