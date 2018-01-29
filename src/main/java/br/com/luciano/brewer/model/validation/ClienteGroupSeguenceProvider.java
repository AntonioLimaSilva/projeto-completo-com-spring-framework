package br.com.luciano.brewer.model.validation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import br.com.luciano.brewer.model.Cliente;

public class ClienteGroupSeguenceProvider implements DefaultGroupSequenceProvider<Cliente> {

	@Override
	public List<Class<?>> getValidationGroups(Cliente cliente) {
		List<Class<?>> grupos = new ArrayList<>();
		grupos.add(Cliente.class);
		
		if(temClienteSelecionado(cliente)) {
			grupos.add(cliente.getTipoPessoa().getGrupo());
		}
		
		return grupos;
	}

	private boolean temClienteSelecionado(Cliente cliente) {
		return cliente != null && cliente.getTipoPessoa() != null;
	}

}
