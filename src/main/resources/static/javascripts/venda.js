Brewer.Venda = (function(){
	
	function Venda(tabelaItens) {
		this.tabelaItens = tabelaItens;
		this.valorTotalBox = $('.js-valor-total-box');
		this.valorFreteInput = $('#valorFrete');
		this.valorDescontoInput = $('#valorDesconto');
		this.valorTotalContainer = $('.js-valor-total-container');
		
		this.valorTotalItens = this.tabelaItens.valorTotalItens();
		this.valorFrete = this.valorFreteInput.data('valor');
		this.valorDesconto = this.valorDescontoInput.data('valor');
	}
	
	Venda.prototype.iniciar = function() {
		this.tabelaItens.on('tabela-item-atualizada', onTabelaItensAtualizada.bind(this));
		this.valorFreteInput.on('keyup', onValorFreteAlterado.bind(this));
		this.valorDescontoInput.on('keyup', onValorDescontoAlterado.bind(this));
		
		this.tabelaItens.on('tabela-item-atualizada', onValoresAlterados.bind(this));
		this.valorFreteInput.on('keyup', onValoresAlterados.bind(this));
		this.valorDescontoInput.on('keyup', onValoresAlterados.bind(this));
		
		onValoresAlterados.call(this);
	}
	
	function onTabelaItensAtualizada(evento, valorTotal) {
		this.valorTotalItens =  valorTotal == null ? 0 : valorTotal;
	}
	
	function onValorFreteAlterado(evento) {
		this.valorFrete = Brewer.recuperarValor($(evento.target).val());
	}
	
	function onValorDescontoAlterado(evento) {
		this.valorDesconto = Brewer.recuperarValor($(evento.target).val());
	}
		
	function onValoresAlterados() {
		var valorTotal = numeral(this.valorTotalItens) - numeral(this.valorDesconto) + numeral(this.valorFrete);
		this.valorTotalBox.html(Brewer.formatarMoeda(valorTotal));
		
		this.valorTotalContainer.toggleClass('negativo', valorTotal < 0);		
	}
	
	return Venda;
}());

$(function(){
	var autocomplete = new Brewer.AutoComplete();
	autocomplete.iniciar();
	
	var tabelaItens = new Brewer.TabelaItens(autocomplete);
	tabelaItens.iniciar();
	
	var venda = new Brewer.Venda(tabelaItens);
	venda.iniciar();
});