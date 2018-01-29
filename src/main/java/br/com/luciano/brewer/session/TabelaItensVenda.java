package br.com.luciano.brewer.session;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import br.com.luciano.brewer.model.Cerveja;
import br.com.luciano.brewer.model.ItemVenda;

class TabelaItensVenda {

	private String uuid;
	private List<ItemVenda> itens = new ArrayList<>();

	public TabelaItensVenda(String uuid) {
		this.uuid = uuid;
	}

	public BigDecimal getValorTotal() {
		return this.itens.stream().map(ItemVenda::getValorTotal).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
	}

	public void adicionar(Cerveja cerveja, Integer quantidade) {
		Optional<ItemVenda> itemVendaOptional = buscarItemAdicionado(cerveja);

		ItemVenda item = null;
		if (itemVendaOptional.isPresent()) {
			item = itemVendaOptional.get();
			item.setQuantidade(item.getQuantidade() + quantidade);
		} else {
			item = new ItemVenda();
			item.setCerveja(cerveja);
			item.setQuantidade(quantidade);
			item.setValorUnitario(cerveja.getValor());
			this.itens.add(0, item);
		}
	}

	public void alterarQuantidade(Cerveja cerveja, Integer quantidade) {
		ItemVenda item = buscarItemAdicionado(cerveja).get();
		item.setQuantidade(quantidade);
	}

	public void excluir(Cerveja cerveja) {
		int indice = IntStream.range(0, this.itens.size()).filter(i -> itens.get(i).getCerveja().equals(cerveja))
				.findAny().getAsInt();

		itens.remove(indice);
	}

	public Optional<ItemVenda> buscarItemAdicionado(Cerveja cerveja) {
		return this.itens.stream().filter(i -> i.getCerveja().equals(cerveja)).findAny();
	}

	public List<ItemVenda> getItens() {
		return this.itens;
	}

	public String getUuid() {
		return uuid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TabelaItensVenda other = (TabelaItensVenda) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

}
