package br.com.luciano.brewer.repository.helper.util;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PaginacaoUtil {
	
	public void preparar(Pageable pageable, Criteria criteria) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistroPorPagina = pageable.getPageSize();
		int primeiroRegistro = paginaAtual * totalRegistroPorPagina;
		
		criteria.setFirstResult(primeiroRegistro);
		criteria.setMaxResults(totalRegistroPorPagina);
		
		Sort sort = pageable.getSort();
		if(sort != null) {
			Sort.Order order = sort.iterator().next();
			String campo = order.getProperty();
			criteria.addOrder(order.isAscending() ? Order.asc(campo) : Order.desc(campo));
		}
	}
}