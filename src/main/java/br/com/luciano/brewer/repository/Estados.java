package br.com.luciano.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.luciano.brewer.model.Estado;

@Repository
public interface Estados extends JpaRepository<Estado, Integer> {

}
