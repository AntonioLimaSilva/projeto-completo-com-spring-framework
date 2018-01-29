package br.com.luciano.brewer.repository.helper.cerveja;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.luciano.brewer.dto.CervejaDTO;
import br.com.luciano.brewer.model.Cerveja;
import br.com.luciano.brewer.repository.filter.CervejaFilter;

public interface CervejasQueries {

	public abstract Page<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable);
	
	public abstract List<CervejaDTO> porSkuOuNome(String skuOuNome);
}
