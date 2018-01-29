package br.com.luciano.brewer.service.event.venda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.luciano.brewer.model.Cerveja;
import br.com.luciano.brewer.model.ItemVenda;
import br.com.luciano.brewer.repository.Cervejas;

@Component
public class VendaListener {
	
	@Autowired
	private Cervejas cervejas;
	
	@EventListener
	public void vendaEmitida(VendaEvent vendaEvent) {
		for(ItemVenda item : vendaEvent.getVenda().getItens()) {
			Cerveja cerveja = this.cervejas.findOne(item.getCerveja().getId());
			cerveja.setQuantidadeEstoque(cerveja.getQuantidadeEstoque() - item.getQuantidade());
			
			cervejas.save(cerveja);
		}
	}

}
