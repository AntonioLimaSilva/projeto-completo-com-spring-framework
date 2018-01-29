Brewer = Brewer || {};

Brewer.DialogoExcluir = (function(){
	
	function DialogoExcluir() {
		this.botao = $('.js-excluir-btn');
	}
	
	DialogoExcluir.prototype.iniciar = function() {
		this.botao.on('click', onExcluirClicado.bind(this));
		
		if(window.location.search.indexOf('excluido') > -1) {
			swal('Pronto!', 'Excluído com sucesso!', 'success');
		}
	}
	
	function onExcluirClicado(evento) {
		evento.preventDefault();
		
		var botaoClicado = $(evento.currentTarget);		
		var url = botaoClicado.data('url');
		var objeto = botaoClicado.data('objeto');
		
		swal({
			title: 'Tem certeza?',
			text: 'Excluir "' + objeto + '"? Você não poderá recuperar depois.',
			showCancelButton: true,
			confirmButtonColor: '#DD6B55',
			confirmButtonText: 'Sim, exclua agora!',
			closeOnConfirm: false,
		}, onExcluirConfirmado.bind(this, url));
	}
	
	function onExcluirConfirmado(url) {
		$.ajax({
			url: url,
			method: 'DELETE',
			success: onExcluidoSucesso.bind(this),
			error: onErroExcluindo.bind(this)
		});		
	}
	
	function onExcluidoSucesso() {
		var urlAtual = window.location.href;
		var separador = urlAtual.indexOf('?') > -1 ? '&' : '?';
		var novaUrl = urlAtual.indexOf('excluido') > -1 ? urlAtual : urlAtual + separador + 'excluido';
	
		window.location = novaUrl;
	}
	
	function onErroExcluindo(evento) {
		console.log('Oops! ', evento.responseText, 'error');
		swal('Oops! ', evento.responseText, 'error')
	}
	
	return DialogoExcluir;
	
}());

$(function(){
	var dialogoExcluir = new Brewer.DialogoExcluir();
	dialogoExcluir.iniciar();
});