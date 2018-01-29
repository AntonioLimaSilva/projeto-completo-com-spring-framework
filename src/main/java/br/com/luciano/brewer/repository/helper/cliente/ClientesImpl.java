package br.com.luciano.brewer.repository.helper.cliente;

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
import org.springframework.util.StringUtils;

import br.com.luciano.brewer.model.Cliente;
import br.com.luciano.brewer.repository.filter.ClienteFilter;
import br.com.luciano.brewer.repository.helper.util.PaginacaoUtil;

public class ClientesImpl implements ClientesQueries {
	
	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<Cliente> filtar(ClienteFilter filtro, Pageable pageable) {
		Criteria criteria = this.manager.unwrap(Session.class).createCriteria(Cliente.class);
		criteria.createAlias("endereco.cidade", "c", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("c.estado", "e", JoinType.LEFT_OUTER_JOIN);
		
		this.paginacaoUtil.preparar(pageable, criteria);
		adicionarFiltro(criteria, filtro);
		
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}

	private void adicionarFiltro(Criteria criteria, ClienteFilter filtro) {
		if(filtro != null) {
			if(!StringUtils.isEmpty(filtro.getNome())) {
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}
			
			if(!StringUtils.isEmpty(filtro.getCpfOuCnpj())) {
				criteria.add(Restrictions.eq("cpfOuCnpj", filtro.getCpfOuCnpj()));
			}
		}
	}

	private Long total(ClienteFilter filtro) {
		Criteria criteria = this.manager.unwrap(Session.class).createCriteria(Cliente.class);
		criteria.setProjection(Projections.rowCount());
		adicionarFiltro(criteria, filtro);
		return (Long) criteria.uniqueResult();
	}

	
}
