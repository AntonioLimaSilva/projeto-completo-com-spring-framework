package br.com.luciano.brewer.repository.helper.cidade;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import br.com.luciano.brewer.model.Cidade;
import br.com.luciano.brewer.repository.filter.CidadeFilter;
import br.com.luciano.brewer.repository.helper.util.PaginacaoUtil;

public class CidadesImpl implements CidadesQueries {
	
	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<Cidade> filtrar(CidadeFilter filtro, Pageable pageable) {
		Criteria criteria = this.manager.unwrap(Session.class).createCriteria(Cidade.class);
		criteria.createAlias("estado", "e", JoinType.LEFT_OUTER_JOIN);
		
		this.paginacaoUtil.preparar(pageable, criteria);
		adicionarFiltro(criteria, filtro);
		
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	@Transactional(readOnly = true)
	@Override
	public Cidade buscarComEstado(Integer id) {
		Criteria criteria = this.manager.unwrap(Session.class).createCriteria(Cidade.class);
		criteria.createAlias("estado", "e", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("id", id));
		return (Cidade) criteria.uniqueResult();
	}
	
	private void adicionarFiltro(Criteria criteria, CidadeFilter filtro) {
		if(filtro != null) {
			if(filtro.getEstado() != null) {
				criteria.add(Restrictions.eq("estado", filtro.getEstado()));
			}
			
			if(filtro.getNome() != null) {
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}
		}
	}

	private Long total(CidadeFilter filtro) {
		Criteria criteria = this.manager.unwrap(Session.class).createCriteria(Cidade.class);
		criteria.setProjection(Projections.rowCount());
		
		adicionarFiltro(criteria, filtro);
		
		return (Long) criteria.uniqueResult();
	}


}
