var Brewer = Brewer || {};

Brewer.MaskMoney = (function(){
	
	function MaskMoney() {
		this.decimal = $('.js-decimal');
		this.plain = $('.js-plain');
	}
	
	MaskMoney.prototype.iniciar = function() {
		this.decimal.maskMoney({decimal: ',', thousands: '.'});
		this.plain.maskMoney({ precision: 0, thousands: '.' });
	}
	
	return MaskMoney;
	
}());

Brewer.MaskPhoneNumber = (function(){
	
	function MaskPhoneNumber() {
		this.inputPhoneNumber = $('.js-phone-number');
	}
	
	MaskPhoneNumber.prototype.iniciar = function() {
		var maskBehavior = function (val) {
		  return val.replace(/\D/g, '').length === 11 ? '(00) 00000-0000' : '(00) 0000-00009';
		};
		
		var options = {
		  onKeyPress: function(val, e, field, options) {
		      field.mask(maskBehavior.apply({}, arguments), options);
		    }
		};
		
		this.inputPhoneNumber.mask(maskBehavior, options);
	}
	
	return MaskPhoneNumber;
	
}());

Brewer.MaskCepNumber = (function(){
	function MaskCepNumber() {
		this.inputCep = $('.js-cep');
	}
	
	MaskCepNumber.prototype.iniciar = function() {
		this.inputCep.mask('00.000-000');
	}
	
	return MaskCepNumber;
}());

Brewer.MaskDate = (function(){
	
	function MaskDate() {
		this.inputDate = $('.js-date');
	}
	
	MaskDate.prototype.iniciar = function() {
		this.inputDate.mask('00/00/0000');
		this.inputDate.datepicker({
			orientation: 'bottom',
			language: 'pt-BR',
			autoclose: true
		});
	}
	
	return MaskDate;
}());

Brewer.Security = (function(){
	
	function Security() {
		this.header = $('input[name=_csrf_header]').val();
		this.token = $('input[name=_csrf]').val();
	}
	
	Security.prototype.iniciar = function() {
		$(document).ajaxSend(function(event, jqxhr, settings){
			jqxhr.setRequestHeader(this.header, this.token);
		}.bind(this));
	}
	
	return Security;
	
}());

numeral.language('pt-br');

Brewer.formatarMoeda = function(valor) {
	return numeral(valor).format('0,0.00');
}

Brewer.recuperarValor = function(valorFormatado) {
	return numeral().unformat(valorFormatado);
}

$(function() {	
	var maskMoney = new Brewer.MaskMoney();
	maskMoney.iniciar();
	
	var maskPhoneNumber = new Brewer.MaskPhoneNumber();
	maskPhoneNumber.iniciar();
	
	var maskCepNumber = new Brewer.MaskCepNumber();
	maskCepNumber.iniciar();
	
	var maskDate = new Brewer.MaskDate();
	maskDate.iniciar();
	
	var security = new Brewer.Security();
	security.iniciar();
});