package br.com.luciano.brewer.service;

import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import br.com.luciano.brewer.model.Usuario;
import br.com.luciano.brewer.repository.Usuarios;
import br.com.luciano.brewer.service.exception.IlegalEmailException;
import br.com.luciano.brewer.service.exception.IlegalSenhaException;
import br.com.luciano.brewer.service.exception.NegocioException;

@Service
public class CadastroUsuarioService {
	
	@Autowired
	private Usuarios usuarios;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional
	public void salvar(Usuario usuario) {
		Optional<Usuario> usuarioExistente = this.usuarios.findByEmailIgnoreCase(usuario.getEmail());
		if(usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
			throw new IlegalEmailException("E-mail já cadastrado");
		}
		
		if(usuario.isNovo() && StringUtils.isEmpty(usuario.getSenha()))
			throw new IlegalSenhaException("Senha é obritagória para novo usuário");
		
		if(usuario.isNovo()) {
			usuario.setSenha(this.passwordEncoder.encode(usuario.getSenha()));
		} else {
			usuario.setSenha(usuarioExistente.get().getSenha());
		}
		usuario.setConfirmacaoSenha(usuario.getSenha());
		
		this.usuarios.save(usuario);
	}

	@Transactional
	public void alterarStatus(Integer[] ids, StatusUsuario statusUsuario) {
		statusUsuario.executar(ids, usuarios);
	}

	@Transactional
	public void excluir(Integer id) {
		try {
			this.usuarios.delete(id);
			this.usuarios.flush();
		} catch(PersistenceException e) {
			throw new NegocioException("Erro tentando excluir usuário");
		}	
	}
	
}
