package br.com.luciano.brewer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.luciano.brewer.model.Cidade;
import br.com.luciano.brewer.repository.helper.cidade.CidadesQueries;

@Repository
public interface Cidades extends JpaRepository<Cidade, Integer>, CidadesQueries {
	
	List<Cidade> findByEstadoId(Integer idEstado);
	
	Optional<Cidade> findByEstadoIdAndNomeIgnoreCase(Integer idEstado, String nome);

}
