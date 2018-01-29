package br.com.luciano.brewer.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import br.com.luciano.brewer.model.Estado;

public class EstadoConverter implements Converter<String, Estado> {

	@Override
	public Estado convert(String value) {
		if(!StringUtils.isEmpty(value)) {
			Estado estado = new Estado();
			estado.setId(Integer.valueOf(value));
			return estado;
		}
		return null;
	}

}
