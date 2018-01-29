package br.com.luciano.brewer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.luciano.brewer.model.Usuario;
import br.com.luciano.brewer.repository.helper.usuario.UsuariosQueries;

@Repository
public interface Usuarios extends JpaRepository<Usuario, Integer>, UsuariosQueries {

	Optional<Usuario> findByEmailIgnoreCase(String email);

	List<Usuario> findByIdIn(Integer[] ids);

}
