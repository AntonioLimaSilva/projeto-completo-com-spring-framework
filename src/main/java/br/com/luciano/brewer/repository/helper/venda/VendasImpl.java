package br.com.luciano.brewer.repository.helper.venda;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import br.com.luciano.brewer.dto.ValorItensEstoque;
import br.com.luciano.brewer.dto.VendaMes;
import br.com.luciano.brewer.dto.VendaOrigem;
import br.com.luciano.brewer.model.Status;
import br.com.luciano.brewer.model.Venda;
import br.com.luciano.brewer.repository.filter.VendaFilter;
import br.com.luciano.brewer.repository.helper.util.PaginacaoUtil;

public class VendasImpl implements VendasQueries {
	
	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<Venda> filtrar(VendaFilter filtro, Pageable pageable) {
		Criteria criteria = this.manager.unwrap(Session.class).createCriteria(Venda.class);
		
		adicionarFiltro(criteria, filtro);
		this.paginacaoUtil.preparar(pageable, criteria);
		
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	@Override
	public BigDecimal valorTotalNoAno() {
		String query = "select sum(valorTotal) from Venda where year(dataCriacao) = :ano and status = :status";
		return Optional.ofNullable(this.manager.createQuery(query, BigDecimal.class)
				.setParameter("ano", Year.now().getValue())
				.setParameter("status", Status.EMITIDO)
				.getSingleResult()).orElse(BigDecimal.ZERO);
	}
	@Override
	public BigDecimal valorTotalNoMes() {
		String query = "select sum(valorTotal) from Venda where month(dataCriacao) = :ano and status = :status";
		return Optional.ofNullable(this.manager.createQuery(query, BigDecimal.class)
				.setParameter("ano", MonthDay.now().getMonthValue())
				.setParameter("status", Status.EMITIDO)
				.getSingleResult()).orElse(BigDecimal.ZERO);
	}
	
	@Override
	public BigDecimal ticketMedioNoAno() {
		String query = "select sum(valorTotal) / count(*) from Venda where year(dataCriacao) = :ano and status = :status";
		return Optional.ofNullable(this.manager.createQuery(query, BigDecimal.class)
				.setParameter("ano", Year.now().getValue())
				.setParameter("status", Status.EMITIDO)
				.getSingleResult()).orElse(BigDecimal.ZERO);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Venda buscarComItens(Integer id) {
		Criteria criteria = this.manager.unwrap(Session.class).createCriteria(Venda.class);
		criteria.createAlias("itens", "i", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("id", id));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (Venda) criteria.uniqueResult();
	}
	
	@Override
	public List<VendaMes> totalPorMes() {
		List<VendaMes> vendasMes = this.manager.createNamedQuery("Vendas.totalPorMes", VendaMes.class).getResultList();
		
		LocalDate hoje = LocalDate.now();
		for(int i = 1; i <= 6; i++) {
			String mesIdeal = String.format("%d/%02d", hoje.getYear(), hoje.getMonthValue());
			
			boolean possuiMes = vendasMes.stream().filter(v -> v.getMes().equals(mesIdeal)).findAny().isPresent();
			if(!possuiMes) {
				vendasMes.add(i -1, new VendaMes(mesIdeal, 0));
			}
			
			hoje = hoje.minusMonths(1);
		}
		
		return vendasMes;
	}
	
	@Override
	public List<VendaOrigem> totalPorOrigem() {
		List<VendaOrigem> vendasOrigem = this.manager.createNamedQuery("Vendas.porOrigem", VendaOrigem.class).getResultList();
		
		LocalDate hoje = LocalDate.now();
		for(int i = 1; i <= 6; i++) {
			String mesIdeal = String.format("%d/%02d", hoje.getYear(), hoje.getMonthValue());
			
			boolean possuiMes = vendasOrigem.stream().filter(v -> v.getMes().equals(mesIdeal)).findAny().isPresent();
			if(!possuiMes) {
				vendasOrigem.add(i -1, new VendaOrigem(mesIdeal, Integer.valueOf(0), Integer.valueOf(0)));
			}
			
			hoje = hoje.minusMonths(1);
		}
		
		return vendasOrigem;
	}
	
	@Override
	public ValorItensEstoque valorItensEstoque() {
		String query = "select new br.com.luciano.brewer.dto.ValorItensEstoque(sum(valor), sum(quantidadeEstoque)) from Cerveja";
		return Optional.ofNullable(this.manager.createQuery(query, ValorItensEstoque.class)
				.getSingleResult()).orElse(new ValorItensEstoque(BigDecimal.ZERO, Long.valueOf(0)));
	}

	private Long total(VendaFilter filtro) {
		Criteria criteria = this.manager.unwrap(Session.class).createCriteria(Venda.class);
		adicionarFiltro(criteria, filtro);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}
	
	private void adicionarFiltro(Criteria criteria, VendaFilter filtro) {		
		if (filtro != null) {
			if(filtro.getStatus() != null) {
				criteria.add(Restrictions.eq("status", filtro.getStatus()));
			}
		}
	}

}
