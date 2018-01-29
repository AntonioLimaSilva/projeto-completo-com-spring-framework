Brewer.TabelaItens = (function(){
	
	function TabelaItens(autocomplete) {
		this.autocomplete = autocomplete;
		this.tabelaItemContainer = $('.js-tabela-item-container');
		this.uuid = $('#uuid').val();
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
	}
	
	TabelaItens.prototype.valorTotalItens = function() {
		return this.tabelaItemContainer.data('valor');
	}
	
	TabelaItens.prototype.iniciar = function() {
		this.autocomplete.on('item-selecionado', onItemSelecionado.bind(this));
		bindQuantidade.call(this);
		bindTabelaItens.call(this);
	}
	
	function onItemSelecionado(evento, item) {
		var resposta = $.ajax({
			url: 'item',
			method: 'POST',
			data: {
				idCerveja: item.id,
				uuid: this.uuid
			}
		});
		
		resposta.done(onItemAtualizadoServidor.bind(this));
	}
	
	function onItemAtualizadoServidor(html) {
		this.tabelaItemContainer.html(html);
		
		bindQuantidade.call(this);		
		var tabelaItem = bindTabelaItens.call(this);
			
		this.emitter.trigger('tabela-item-atualizada', tabelaItem.data('valor-total'));
	}
	
	function onQuantidadeItemAlterado(evento) {
		var input = $(evento.target);
		var quantidade = input.val();
		var idCerveja = input.data('id-cerveja');
		
		if(quantidade <= 0) {
			input.val(1);
			quantidade = 1;
		}
		
		var resposta = $.ajax({
			url: 'item/' + idCerveja,
			method: 'PUT',
			data: {
				quantidade: quantidade,
				uuid: this.uuid
			}
		});
		
		resposta.done(onItemAtualizadoServidor.bind(this));
	}
	
	function onDoubleClick(evento) {
		var value = $(evento.currentTarget);
		value.toggleClass('solicitando-exclusao');	
	}
	
	function onExcluirItemClicado(evento) {
		var idCerveja = $(evento.target).data('id-cerveja');
		$.ajax({
			url: 'item/' + this.uuid + '/' + idCerveja,
			method: 'DELETE',
			success: onItemAtualizadoServidor.bind(this)
		});
	}
	
	function bindQuantidade() {
		var quantidadeItemInput = $('.js-quantidade-itens-venda');
		quantidadeItemInput.on('change', onQuantidadeItemAlterado.bind(this));
		quantidadeItemInput.maskMoney({precision: 0, thousands: ''});
	}
	
	function bindTabelaItens() {
		var tabelaItem = $('.js-tabela-item');		
		tabelaItem.on('dblclick', onDoubleClick);
		$('.js-exclusao-item-btn').on('click', onExcluirItemClicado.bind(this));
		
		return tabelaItem;
	}
	
	return TabelaItens;
}());