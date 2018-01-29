package br.com.luciano.brewer.service;

import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.luciano.brewer.model.Cidade;
import br.com.luciano.brewer.repository.Cidades;
import br.com.luciano.brewer.service.exception.NegocioException;

@Service
public class CadastroCidadeService {

	@Autowired
	private Cidades cidades;
	
	@Transactional
	public void salvar(Cidade cidade) {
		Optional<Cidade> cidadeExistente = this.cidades.findByEstadoIdAndNomeIgnoreCase(cidade.getEstado().getId(), cidade.getNome());
		
		if(cidadeExistente.isPresent()) {
			throw new NegocioException("Cidade já esta cadastrada!");
		}
		
		this.cidades.save(cidade);
	}

	@Transactional
	public void excluir(Integer id) {
		try {
			this.cidades.delete(id);
			this.cidades.flush();
		} catch(PersistenceException e) {
			throw new NegocioException("Erro tentando exluir cidade " + e);
		}
	}
	
}
