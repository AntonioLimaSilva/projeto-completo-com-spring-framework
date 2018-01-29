package br.com.luciano.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.luciano.brewer.model.Venda;
import br.com.luciano.brewer.repository.helper.venda.VendasQueries;

@Repository
public interface Vendas extends JpaRepository<Venda, Integer>, VendasQueries {

}
