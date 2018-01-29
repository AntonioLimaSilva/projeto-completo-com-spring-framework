package br.com.luciano.brewer.repository.helper.cerveja;

import java.util.List;

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
import org.springframework.util.StringUtils;

import br.com.luciano.brewer.dto.CervejaDTO;
import br.com.luciano.brewer.model.Cerveja;
import br.com.luciano.brewer.repository.filter.CervejaFilter;
import br.com.luciano.brewer.repository.helper.util.PaginacaoUtil;

public class CervejasImpl implements CervejasQueries {
	
	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable) {
		Criteria criteria = this.manager.unwrap(Session.class).createCriteria(Cerveja.class);
		
		this.paginacaoUtil.preparar(pageable, criteria);
		
		adicionarFiltro(criteria, filtro);
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}

	@Override
	public List<CervejaDTO> porSkuOuNome(String skuOuNome) {
		String jpql = "select new br.com.luciano.brewer.dto.CervejaDTO(id, sku, nome, origem, valor, nomeFoto) "
				+ "from Cerveja where lower(sku) like lower(:skuOuNome) or lower(nome) like lower(:skuOuNome)";
		
		return this.manager.createQuery(jpql, CervejaDTO.class)
				.setParameter("skuOuNome", skuOuNome.concat("%"))
				.getResultList();
	}
	
	private Long total(CervejaFilter filtro) {
		Criteria criteria = this.manager.unwrap(Session.class).createCriteria(Cerveja.class);
		adicionarFiltro(criteria, filtro);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(Criteria criteria, CervejaFilter filtro) {
		if(filtro != null) {
			if(!StringUtils.isEmpty(filtro.getSku())) {
				criteria.add(Restrictions.ilike("sku", filtro.getSku()));
			}
			
			if(!StringUtils.isEmpty(filtro.getNome())) {
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}
			
			if(filtro.getEstilo() != null && !StringUtils.isEmpty(filtro.getEstilo().getId())) {
				criteria.createAlias("estilo", "e");
				criteria.add(Restrictions.eq("e.id", filtro.getEstilo().getId()));
			}
			
			if(filtro.getSabor() != null) {
				criteria.add(Restrictions.eq("sabor", filtro.getSabor()));
			}
			
			if(filtro.getOrigem() != null) {
				criteria.add(Restrictions.eq("origem", filtro.getOrigem()));
			}
			
			if(filtro.getValorDe() != null && filtro.getValorAte() != null) {
				criteria.add(Restrictions.between("valor", filtro.getValorDe(), filtro.getValorAte()));
			}
		}
	}

}
