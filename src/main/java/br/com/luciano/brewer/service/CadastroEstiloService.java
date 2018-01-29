package br.com.luciano.brewer.service;

import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.luciano.brewer.model.Estilo;
import br.com.luciano.brewer.repository.Estilos;
import br.com.luciano.brewer.service.exception.NegocioException;

@Service
public class CadastroEstiloService {
	
	@Autowired
	private Estilos estilos;
	
	@Transactional
	public Estilo salvar(Estilo estilo) {
		Optional<Estilo> estiloOptional = this.estilos.findByNomeIgnoreCase(estilo.getNome());		
		if(estiloOptional.isPresent() && !estiloOptional.get().equals(estilo))
			throw new NegocioException("Estilo já cadastrado");
		
		return this.estilos.saveAndFlush(estilo);
	}

	@Transactional
	public void excluir(Integer id) {
		try {
			this.estilos.delete(id);
			this.estilos.flush();
		} catch(PersistenceException e) {
			throw new NegocioException("Erro tentando excluir estilo " + e);
		}
	}
	
}
