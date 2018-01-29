package br.com.luciano.brewer.service;

import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.luciano.brewer.model.Cliente;
import br.com.luciano.brewer.repository.Clientes;
import br.com.luciano.brewer.service.exception.NegocioException;

@Service
public class CadastroClienteService {
	
	@Autowired
	private Clientes clientes;
	
	@Transactional
	public void salvar(Cliente cliente) {
		Optional<Cliente> clienteExistente = this.clientes.findByCpfOuCnpj(cliente.getCpfCnpjSemFormatacao());
		if(clienteExistente.isPresent()) {
			throw new NegocioException("Cliente já cadastrado");
		}
		
		this.clientes.save(cliente);
	}

	@Transactional
	public void excluir(Integer id) {
		try {
			this.clientes.delete(id);
			this.clientes.flush();
		} catch(PersistenceException e) {
			throw new NegocioException("Erro tentando excluir cliente " + e);
		}
	}

}