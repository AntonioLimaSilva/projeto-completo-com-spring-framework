package br.com.luciano.brewer.repository.helper.estilo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.luciano.brewer.model.Estilo;
import br.com.luciano.brewer.repository.filter.EstiloFilter;

public interface EstilosQueries {

	Page<Estilo> filtrar(EstiloFilter estiloFilter, Pageable pageable);

}
