package br.com.luciano.brewer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import br.com.luciano.brewer.service.CadastroCervejaService;
import br.com.luciano.brewer.storage.FotoStorage;
import br.com.luciano.brewer.storage.local.FotoStorageLocal;

@Configuration
@ComponentScan(basePackageClasses = CadastroCervejaService.class)
public class ServiceConfig {
	
	@Bean
	public FotoStorage fotoStorage() {
		return new FotoStorageLocal();
	}

}
