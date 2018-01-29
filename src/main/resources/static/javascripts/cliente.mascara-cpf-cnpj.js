var Brewer = Brewer || {};

Brewer.MascaraCpfCnpj = (function(){
	
	function MascaraCpfCnpj() {
		this.radioTipoPessoa = $('.js-radio-tipo-pessoa');
		this.labelCpfCnpj = $('[for=cpfOuCnpj]');
		this.inputCpfOuCnpj = $('#cpfOuCnpj');
	}
	
	MascaraCpfCnpj.prototype.iniciar = function() {
		this.radioTipoPessoa.on('change', onRadioTipoPessoaSelecionado.bind(this));
		
		var tipoPessoaSelecionada = this.radioTipoPessoa.filter(':checked')[0];
		if(tipoPessoaSelecionada) {
			aplicarMascara.call(this, $(tipoPessoaSelecionada));
		}
	}
	
	function onRadioTipoPessoaSelecionado(evento) {
		var tipoPessoaSelecionada = $(evento.currentTarget);
		this.inputCpfOuCnpj.val('');		
		
		aplicarMascara.call(this, tipoPessoaSelecionada);
	}
	
	function aplicarMascara(tipoPessoaSelecionada) {
		this.labelCpfCnpj.text(tipoPessoaSelecionada.data('documento'));
		this.inputCpfOuCnpj.removeAttr('disabled');
		this.inputCpfOuCnpj.mask(tipoPessoaSelecionada.data('mascara'));		
	}
	
	return MascaraCpfCnpj;
	
}());

$(function(){
	var mascaraCpfCnpj = new Brewer.MascaraCpfCnpj();
	mascaraCpfCnpj.iniciar();
});