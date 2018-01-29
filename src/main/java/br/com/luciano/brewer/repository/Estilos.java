package br.com.luciano.brewer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.luciano.brewer.model.Estilo;
import br.com.luciano.brewer.repository.helper.estilo.EstilosQueries;

@Repository
public interface Estilos extends JpaRepository<Estilo, Integer>, EstilosQueries {
	
	Optional<Estilo> findByNomeIgnoreCase(String nome); 

}
