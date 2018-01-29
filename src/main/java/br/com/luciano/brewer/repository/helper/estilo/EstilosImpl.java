package br.com.luciano.brewer.repository.helper.estilo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import br.com.luciano.brewer.model.Estilo;
import br.com.luciano.brewer.repository.filter.EstiloFilter;
import br.com.luciano.brewer.repository.helper.util.PaginacaoUtil;

public class EstilosImpl implements EstilosQueries {
	
	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<Estilo> filtrar(EstiloFilter estiloFilter, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Estilo.class);
		
		this.paginacaoUtil.preparar(pageable, criteria);
		
		adicionarFiltro(criteria, estiloFilter);
		
		return new PageImpl<>(criteria.list(), pageable, total(estiloFilter));
	}
	
	private void adicionarFiltro(Criteria criteria, EstiloFilter estiloFilter) {
		if(estiloFilter != null) {
			if(estiloFilter.getNome() != null) {
				criteria.add(Restrictions.ilike("nome", estiloFilter.getNome(), MatchMode.ANYWHERE));
			}
		}
	}

	private Long total(EstiloFilter estiloFilter) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Estilo.class);
		adicionarFiltro(criteria, estiloFilter);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

}
