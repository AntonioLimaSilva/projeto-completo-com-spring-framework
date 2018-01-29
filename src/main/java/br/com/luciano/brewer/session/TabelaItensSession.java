package br.com.luciano.brewer.session;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import br.com.luciano.brewer.model.Cerveja;
import br.com.luciano.brewer.model.ItemVenda;

@SessionScope
@Component
public class TabelaItensSession {

	private Set<TabelaItensVenda> itens = new HashSet<>();

	public void adicionar(String uuid, Cerveja cerveja, int quantidade) {
		TabelaItensVenda tabela = buscarTabelaItemVenda(uuid);
		tabela.adicionar(cerveja, quantidade);
		this.itens.add(tabela);
	}

	public void alterarQuantidade(String uuid, Cerveja cerveja, Integer quantidade) {
		TabelaItensVenda tabela = buscarTabelaItemVenda(uuid);
		tabela.alterarQuantidade(cerveja, quantidade);
		this.itens.add(tabela);
	}

	public void excluir(String uuid, Cerveja cerveja) {
		TabelaItensVenda tabela = buscarTabelaItemVenda(uuid);
		tabela.excluir(cerveja);
		this.itens.add(tabela);
	}

	public List<ItemVenda> getItens(String uuid) {
		return buscarTabelaItemVenda(uuid).getItens();
	}	
	
	private TabelaItensVenda buscarTabelaItemVenda(String uuid) {
		return itens.stream().filter(t -> t.getUuid().equals(uuid)).findAny().orElse(new TabelaItensVenda(uuid));
	}

	public BigDecimal getValorTotal(String uuid) {
		return buscarTabelaItemVenda(uuid).getValorTotal();
	}
	
}
