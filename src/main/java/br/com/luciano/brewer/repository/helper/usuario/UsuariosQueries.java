package br.com.luciano.brewer.repository.helper.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.luciano.brewer.model.Usuario;
import br.com.luciano.brewer.repository.filter.UsuarioFilter;

public interface UsuariosQueries {
	
	Page<Usuario> filtrar(UsuarioFilter filtro, Pageable pageable);
	
	Optional<Usuario> buscarPorEmalAtivo(String email);
	
	List<String> buscarPermissoes(Usuario usuario);
	
	Usuario buscarComGrupos(Integer id);

}
