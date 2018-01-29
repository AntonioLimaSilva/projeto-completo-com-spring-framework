package br.com.luciano.brewer.repository.helper.venda;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.luciano.brewer.dto.ValorItensEstoque;
import br.com.luciano.brewer.dto.VendaMes;
import br.com.luciano.brewer.dto.VendaOrigem;
import br.com.luciano.brewer.model.Venda;
import br.com.luciano.brewer.repository.filter.VendaFilter;

public interface VendasQueries {
	
	Page<Venda> filtrar(VendaFilter filtro, Pageable pageable);
	
	Venda buscarComItens(Integer id);
	
	BigDecimal valorTotalNoAno();
	
	BigDecimal valorTotalNoMes();
	
	BigDecimal ticketMedioNoAno();
	
	List<VendaMes> totalPorMes();
	
	List<VendaOrigem> totalPorOrigem();
	
	ValorItensEstoque valorItensEstoque();

}
