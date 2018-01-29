Brewer = Brewer || {};

Brewer.PesquisaRapidaCliente = (function(){
	
	function PesquisaRapidaCliente() {
		this.pesquisaRapidaClientesModal = $('#pesquisaRapidaClientes');
		this.pesquisaRapidaBtn = $('.js-pesquisa-rapida-btn');
		this.nomeClienteInput = $('#nomeClienteModal');
		this.mensagemErro = $('.js-mensagem-erro');
		this.htmlTabelaPesquisaRapida = $('#tabela-pesquisa-rapida-cliente').html();
		this.template = Handlebars.compile(this.htmlTabelaPesquisaRapida);
		this.containerPesquisaRapida = $('#container-pesquisa-rapida-clientes');		
	}
	
	PesquisaRapidaCliente.prototype.iniciar = function() {
		this.pesquisaRapidaBtn.on('click', onPesquisaClientesClicado.bind(this));
		this.pesquisaRapidaClientesModal.on('shown.bs.modal', onModalExibido.bind(this));
	}
	
	function onModalExibido() {
		this.nomeClienteInput.focus();
	}
	
	function onPesquisaClientesClicado(evento) {
		evento.preventDefault();
		
		$.ajax({
			url: this.pesquisaRapidaClientesModal.find('form').attr('action'),
			method: 'GET',
			contentType: 'application/json',
			data: {
				nome: this.nomeClienteInput.val()
			},
			success: onPesquisaConcluida.bind(this),
			error: onErroPesquisa.bind(this)
		});
	}
	
	function onPesquisaConcluida(resultado) {
		this.mensagemErro.addClass('hidden');
		
		var html = this.template(resultado);
		this.containerPesquisaRapida.html(html);
		
		var tabelaPesquisaCliente = new Brewer.TabelaPesquisaCliente(this.pesquisaRapidaClientesModal);
		tabelaPesquisaCliente.iniciar();
	}
	
	function onErroPesquisa() {
		this.mensagemErro.removeClass('hidden');
	}
	
	return PesquisaRapidaCliente;
	
}());

Brewer.TabelaPesquisaCliente = (function(){
	
	function TabelaPesquisaCliente(modal) {
		this.modalCliente = modal;
		this.cliente = $('.js-tabela-pesquisa-cliente');
	}
	
	TabelaPesquisaCliente.prototype.iniciar = function() {
		this.cliente.on('click', onClienteSelecionado.bind(this));
	}
	
	function onClienteSelecionado(evento) {
		this.modalCliente.modal('hide');
		
		var clienteSelecionado = $(evento.currentTarget);
		$('#nomeCliente').val(clienteSelecionado.data('nome'));
		$('#idCliente').val(clienteSelecionado.data('id'))
	}
	
	return TabelaPesquisaCliente;
	
}());

$(function(){
	
	var pesquisaRapidaCliente = new Brewer.PesquisaRapidaCliente();
	pesquisaRapidaCliente.iniciar();
	
});