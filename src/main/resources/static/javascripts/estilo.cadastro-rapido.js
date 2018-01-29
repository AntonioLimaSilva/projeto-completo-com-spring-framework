var Brewer = Brewer || {};

Brewer.CadastroRapido = (function(){
	
	function CadastroRapido() {
		this.modal = $('#modalCadastroRapidoEstilo');
		this.inputNomeEstilo = $('#nomeEstilo');
		this.btnSalvar = $('.js-btn-salvar-estilo');
		this.form = this.modal.find('form');
		this.form.on('submit', function(evento){evento.preventDefault()});
		this.url = this.form.attr('action');
	}
		
	CadastroRapido.prototype.iniciar = function() {
		this.modal.on('shown.bs.modal', onModalExibido.bind(this));
		this.modal.on('hide.bs.modal', onModalFechado.bind(this));
		this.btnSalvar.on('click', onBotaoSalvarEstilo.bind(this));
	}
	
	function onModalExibido() {
		this.inputNomeEstilo.focus();
	}
	
	function onModalFechado() {
		this.inputNomeEstilo.val('');
	}
	
	function onBotaoSalvarEstilo(evento) {
		$.ajax({		
			url: this.url,
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({nome: this.inputNomeEstilo.val().trim()}),
			error: onErroSalvandoEstilo.bind(this),
			success: onEstiloSalvo.bind(this)
		});
	}
	
	function onErroSalvandoEstilo(obj) {		
		var containerMensagem = $('.js-mensagem-cadastro-rapido-estilo');
		containerMensagem.removeClass('hidden');
		containerMensagem.html(obj.responseText);
	}
	
	function onEstiloSalvo(estilo) {
		var comboEstilo = $('#estilo');
		comboEstilo.append('<option value=' + estilo.id + '>' + estilo.nome + '</option>');
		comboEstilo.val(estilo.id);
		this.modal.modal('hide');
	}
	
	return CadastroRapido;
	
}());

$(function(){	
	var cadastroRapido = new Brewer.CadastroRapido();
	cadastroRapido.iniciar();
});