var Brewer = Brewer || {}

Brewer.UploadFoto = (function(){
	
	function UploadFoto() {
		this.inputNomeFoto = $('input[name=nomeFoto]');
		this.inputContentType = $('input[name=contentType]');	
		this.inputNovaFoto = $('input[name=novaFoto]');
		this.htmlFotoCervejaTemplate = $('#foto-cerveja').html();
		this.template = Handlebars.compile(this.htmlFotoCervejaTemplate);
		this.uploadDrop = $('#upload-drop');						
		this.containerFotoCerveja = $('.js-container-foto-cerveja');		
	}
	
	UploadFoto.prototype.iniciar = function() {
		var settings = {
			type: 'json',
			filelimit: 1,
			allow: '*.(jpg|jpeg|png)',
			action: this.containerFotoCerveja.data('url-fotos'),
			complete: onUploadCompleto.bind(this),
			beforeSend: adiconarCsrfToken.bind(this)
		};
		
		UIkit.uploadSelect($('#upload-select'), settings);
		UIkit.uploadDrop(this.uploadDrop, settings);
		
		if(this.inputNomeFoto.val()) {
			renderizarFoto.call(this, {nome: this.inputNomeFoto.val(), contentType: this.inputContentType.val()});
		}
	}
	
	function onUploadCompleto(resposta) {		
		this.inputNovaFoto.val('true');
		renderizarFoto.call(this, resposta);
	}
	
	function renderizarFoto(resposta) {
		this.inputNomeFoto.val(resposta.nome);
		this.inputContentType.val(resposta.contentType);	
		
		this.uploadDrop.addClass('hidden');		
		
		var foto = '';
		if(this.inputNovaFoto.val() == 'true') {
			foto = 'temp/';
		} else {
			foto = 'local/';
		}
		foto += resposta.nome;
		
		var htmlFotoCerveja = this.template({foto: foto});						
		this.containerFotoCerveja.append(htmlFotoCerveja);
		
		$('.js-remove-foto').on('click', onRemoveFoto.bind(this));		
	}
	
	function onRemoveFoto(evento){
		$('.js-foto-cerveja').remove();
		this.uploadDrop.removeClass('hidden');
		this.inputNomeFoto.val('');
		this.inputContentType.val('');
	}
	
	function adiconarCsrfToken(xhr) {
		var header = $('input[name=_csrf_header]').val();
		var token = $('input[name=_csrf]').val();
		xhr.setRequestHeader(header, token);
	}
	
	return UploadFoto;
	
}());

$(function(){
	var uploadFoto = new Brewer.UploadFoto();
	uploadFoto.iniciar();
});