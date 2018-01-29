package br.com.luciano.brewer.config.init;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import br.com.luciano.brewer.config.JPAConfig;
import br.com.luciano.brewer.config.MailConfig;
import br.com.luciano.brewer.config.SecurityConfig;
import br.com.luciano.brewer.config.ServiceConfig;
import br.com.luciano.brewer.config.WebConfig;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { JPAConfig.class, ServiceConfig.class, SecurityConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[]{ WebConfig.class, MailConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{ "/" };
	}
	
	@Override
	protected Filter[] getServletFilters() {
		// Esse objeto é necessário por conta de uma proteção que o verbo PUT tem, caso não
		// existe esse objeto httpPutFormContentFilter não vai funcionar
		HttpPutFormContentFilter httpPutFormContentFilter = new HttpPutFormContentFilter();
		
		return new Filter[] { httpPutFormContentFilter };
	}
	
	@Override
	protected void customizeRegistration(Dynamic registration) {
		registration.setMultipartConfig(new MultipartConfigElement(""));
	}

}
