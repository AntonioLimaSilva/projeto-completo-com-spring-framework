package br.com.luciano.brewer.service;

import br.com.luciano.brewer.repository.Usuarios;

public enum StatusUsuario {
	
	ATIVAR {
		@Override
		public void executar(Integer[] ids, Usuarios usuarios) {
			usuarios.findByIdIn(ids).forEach(u -> u.setAtivo(true));
		}
	},
	DESATIVAR {
		@Override
		public void executar(Integer[] ids, Usuarios usuarios) {
			usuarios.findByIdIn(ids).forEach(u -> u.setAtivo(false));
		}
	};
	
	public abstract void executar(Integer[] ids, Usuarios usuarios);

}
