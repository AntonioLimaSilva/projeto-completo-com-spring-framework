package br.com.luciano.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.luciano.brewer.model.Grupo;

@Repository
public interface Grupos extends JpaRepository<Grupo, Integer> {

}
