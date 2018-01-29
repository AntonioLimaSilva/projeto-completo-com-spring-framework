package br.com.luciano.brewer.repository.helper.usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import br.com.luciano.brewer.model.Grupo;
import br.com.luciano.brewer.model.Usuario;
import br.com.luciano.brewer.model.UsuarioGrupo;
import br.com.luciano.brewer.repository.filter.UsuarioFilter;
import br.com.luciano.brewer.repository.helper.util.PaginacaoUtil;

public class UsuariosImpl implements UsuariosQueries {
	
	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;

	@Override
	public Optional<Usuario> buscarPorEmalAtivo(String email) {
		return this.manager.createQuery("from Usuario where lower(email) = lower(:email) and ativo = true", Usuario.class)
				.setParameter("email", email)
				.getResultList()
				.stream()
				.findFirst();
	}

	@Override
	public List<String> buscarPermissoes(Usuario usuario) {
		String query = "select distinct p.nome from Usuario u inner join u.grupos g inner join g.permissoes p where u = :usuario";
		return this.manager.createQuery(query, String.class)
				.setParameter("usuario", usuario)
				.getResultList();
	}
	
	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	@Override
	public Page<Usuario> filtrar(UsuarioFilter filtro, Pageable pageable) {
		Criteria criteria = this.manager.unwrap(Session.class).createCriteria(Usuario.class);
		
		adicionarFiltro(filtro, criteria);
		this.paginacaoUtil.preparar(pageable, criteria);
		
		List<Usuario> filtrados = criteria.list();
		//Aqui pedimos para o hibernate inicializar os grupos assim, devido o uso da paginação com o limit
		filtrados.forEach(u -> Hibernate.initialize(u.getGrupos()));
		
		return new PageImpl<>(filtrados, pageable, total(filtro));
	}
	
	@Transactional(readOnly = true)
	@Override
	public Usuario buscarComGrupos(Integer id) {
		Criteria criteria = this.manager.unwrap(Session.class).createCriteria(Usuario.class);
		criteria.createAlias("grupos", "g", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("id", id));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (Usuario) criteria.uniqueResult();
	}

	private Long total(UsuarioFilter filtro) {
		Criteria criteria = this.manager.unwrap(Session.class).createCriteria(Usuario.class);
		
		adicionarFiltro(filtro, criteria);
		
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(UsuarioFilter filtro, Criteria criteria) {
		if(filtro != null) {
			if(!StringUtils.isEmpty(filtro.getNome())) {
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}
			
			if(!StringUtils.isEmpty(filtro.getEmail())) {
				criteria.add(Restrictions.eq("email", filtro.getEmail()));
			}
			
			if(filtro.getGrupos() != null && !filtro.getGrupos().isEmpty()) {
				List<Criterion> subqueries = new ArrayList<>();
				filtro.getGrupos()
					.stream()
					.mapToInt(Grupo::getId)
					.forEach((idGrupo) -> {
						DetachedCriteria dc = DetachedCriteria.forClass(UsuarioGrupo.class);
						dc.add(Restrictions.eq("id.grupo.id", idGrupo));
						dc.setProjection(Projections.property("id.usuario"));
					
					subqueries.add(Subqueries.propertyIn("id", dc));
				});
				
				Criterion[] criterions = new Criterion[subqueries.size()];
				criteria.add(Restrictions.and(subqueries.toArray(criterions)));
			}
		}
		
	}

}
