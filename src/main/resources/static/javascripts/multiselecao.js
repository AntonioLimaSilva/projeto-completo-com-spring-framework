Brewer = Brewer || {};

Brewer.MultiSelecao = (function(){
	
	function MultiSelecao() {
		this.statusBtn = $('.js-status-btn');
		this.selecaoCheckbox = $('.js-selecao');
		this.selecaoTodosCheckbox = $('.js-selecao-todos');
	}
	
	MultiSelecao.prototype.iniciar = function() {
		this.statusBtn.on('click', onStatusBtnClicado.bind(this));
		this.selecaoTodosCheckbox.on('click', onSelecaoTodosClicados.bind(this));
		this.selecaoCheckbox.on('click', onSelecaoClicado.bind(this));
	}
	
	function onStatusBtnClicado(evento) {
		var botaoClicado = $(evento.currentTarget);
		var status = botaoClicado.data('status');
		var url = botaoClicado.data('url');
		
		var checkbokSelecionados = this.selecaoCheckbox.filter(':checked');
		var ids = $.map(checkbokSelecionados, function(c){
			return $(c).data('id');
		});
		
		if(ids.length > 0) {
			$.ajax({
				url: url,
				method: 'PUT',
				data: {
					ids: ids,
					status: status
				},
				success: function() {
					window.location.reload();
				}
				
			});
		}
	}
	
	function onSelecaoClicado() {
		var checkboxSelecionados = this.selecaoCheckbox.filter(':checked');
		this.selecaoTodosCheckbox.prop('checked', checkboxSelecionados.length >= this.selecaoCheckbox.length);
		statusBotaoAcao.call(this, checkboxSelecionados.length > 0);
	}
	
	function onSelecaoTodosClicados() {
		var status = this.selecaoTodosCheckbox.prop('checked');
		this.selecaoCheckbox.prop('checked', status);
		statusBotaoAcao.call(this, status);
	}
	
	function statusBotaoAcao(ativar) {
		ativar ? this.statusBtn.removeClass('disabled') : this.statusBtn.addClass('disabled');
	}
	
	return MultiSelecao;
	
}());

$(function(){
	
	var multiSelecao = new Brewer.MultiSelecao();
	multiSelecao.iniciar();
});