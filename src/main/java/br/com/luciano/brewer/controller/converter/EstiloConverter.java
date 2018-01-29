package br.com.luciano.brewer.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import br.com.luciano.brewer.model.Estilo;

public class EstiloConverter implements Converter<String, Estilo> {

	@Override
	public Estilo convert(String id) {		
		if(!StringUtils.isEmpty(id)) {
			Estilo estilo = new Estilo();
			estilo.setId(Integer.valueOf(id));
			return estilo;
		}
		return null;
	}

}
