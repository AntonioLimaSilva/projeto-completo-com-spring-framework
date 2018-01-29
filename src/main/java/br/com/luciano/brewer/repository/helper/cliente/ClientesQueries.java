package br.com.luciano.brewer.repository.helper.cliente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.luciano.brewer.model.Cliente;
import br.com.luciano.brewer.repository.filter.ClienteFilter;

public interface ClientesQueries {
	
	public Page<Cliente> filtar(ClienteFilter filtro, Pageable pageable);

}
