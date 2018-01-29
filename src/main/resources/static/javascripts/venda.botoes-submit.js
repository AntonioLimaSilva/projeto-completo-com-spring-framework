Brewer = Brewer || {};

Brewer.BotaoSubmit = (function(){
	
	function BotaoSubmit() {
		this.botao = $('.js-venda-btn');
		this.formulario = $('.js-formulario-principal');
	}
	
	BotaoSubmit.prototype.iniciar = function() {
		this.botao.on('click', onSubmeterFormulario.bind(this));
	}
	
	function onSubmeterFormulario(evento) {
		evento.preventDefault();
		
		var botaoClicado = $(evento.target);
		var acao = botaoClicado.data('acao');
		
		var acaoInput = $('<input>');
		acaoInput.attr('name', acao);
		
		this.formulario.append(acaoInput);
		this.formulario.submit();
	}
	
	return BotaoSubmit;
}());

$(function(){
	var botaoSubmit = new Brewer.BotaoSubmit();
	botaoSubmit.iniciar();
});