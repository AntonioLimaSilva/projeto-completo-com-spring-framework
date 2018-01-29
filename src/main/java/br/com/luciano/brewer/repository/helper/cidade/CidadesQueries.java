package br.com.luciano.brewer.repository.helper.cidade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.luciano.brewer.model.Cidade;
import br.com.luciano.brewer.repository.filter.CidadeFilter;

public interface CidadesQueries {
	
	Page<Cidade> filtrar(CidadeFilter filtro, Pageable pageable);
	
	Cidade buscarComEstado(Integer id);

}
