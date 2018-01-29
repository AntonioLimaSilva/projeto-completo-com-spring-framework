Brewer = Brewer || {};

Brewer.AutoComplete = (function(){
	
	function AutoComplete() {
		this.skuOuNomeInput = $('.js-sku-nome-cerveja-input');
		this.HtmlTemplateAutocomplete = $('#template-autocomplete-cerveja').html();
		this.template = Handlebars.compile(this.HtmlTemplateAutocomplete);
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
	}
	
	AutoComplete.prototype.iniciar = function() {
		var options = {
			url: filtrar.bind(this),
			getValue: 'nome',
			minCharNumber: 3,
			requestDelay: 300,
			ajaxSettings: {
				contentType: 'application/json'
			},
			template: {
				type: 'custom',
				method: template.bind(this)
			},
			list: {
				onChooseEvent: itemSelecionado.bind(this)
			}
		};
		
		this.skuOuNomeInput.easyAutocomplete(options);
	}
	
	function template(nome, cerveja) {
		cerveja.valorFormatado = Brewer.formatarMoeda(cerveja.valor);
		return this.template(cerveja);
	}
	
	function itemSelecionado() {
		this.emitter.trigger('item-selecionado', this.skuOuNomeInput.getSelectedItemData());
		this.skuOuNomeInput.val('');
		this.skuOuNomeInput.focus();
	}
	
	function filtrar(skuOuNome) {
		return this.skuOuNomeInput.data('url') + '?skuOuNome=' + skuOuNome;
	}
	
	return AutoComplete;
	
}());