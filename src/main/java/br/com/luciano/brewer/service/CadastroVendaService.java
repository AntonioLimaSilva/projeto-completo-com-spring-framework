package br.com.luciano.brewer.service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.luciano.brewer.model.Status;
import br.com.luciano.brewer.model.Venda;
import br.com.luciano.brewer.repository.Vendas;
import br.com.luciano.brewer.service.event.venda.VendaEvent;
import br.com.luciano.brewer.service.exception.NegocioException;

@Service
public class CadastroVendaService {
	
	@Autowired
	private Vendas vendas;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Transactional
	public void salvar(Venda venda) {
		if(venda.isNova()) {
			venda.setDataCriacao(LocalDateTime.now());
		} else {
			Venda vendaExistente = this.vendas.findOne(venda.getId());
			venda.setDataCriacao(vendaExistente.getDataCriacao());
		}
		
		if(venda.getDataEntrega() != null) {
			venda.setDataHoraEntrega(LocalDateTime.of(venda.getDataEntrega(), venda.getHorarioEntrega() != null ? venda.getHorarioEntrega() : LocalTime.NOON));
		} else {
			venda.setDataHoraEntrega(LocalDateTime.now());
		}
		
		venda.calcularValorTotal();
		
		this.vendas.save(venda);
	}
	
	@Transactional
	public void emitir(Venda venda) {
		venda.setStatus(Status.EMITIDO);		
		salvar(venda);
		
		this.publisher.publishEvent(new VendaEvent(venda));
	}

	@Transactional
	public void cancelar(Venda venda) {
		venda.setStatus(Status.CANCELADO);
		
		salvar(venda);		
	}
	
	@Transactional
	public void excluir(Integer id) {
		try {
			this.vendas.delete(id);
			this.vendas.flush();
		} catch(PersistenceException e) {
			throw new NegocioException("Erro tentando excluir venda");
		}
	}

}
