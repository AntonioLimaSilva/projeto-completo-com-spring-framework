var Brewer = Brewer || {};

Brewer.GraficoVendaPorMes = (function(){
	
	function GraficoVendaPorMes() {
		this.ctx = $('#graficoVendasPorMes')[0].getContext('2d');
	}
	
	GraficoVendaPorMes.prototype.iniciar = function() {
		$.ajax({
			url: 'vendas/totalPorMes',
			method: 'GET',
			success: onDadosRecebidos.bind(this)
		});
	}
	
	function onDadosRecebidos(dados) {
		var meses = [];
		var valores = [];
		
		dados.forEach(function(obj){
			meses.unshift(obj.mes);
			valores.unshift(obj.total);
		});
		
		var graficoVendasPorMes = new Chart(this.ctx, {
		    type: 'line',
		    data: {
		    	labels: meses,
		    	datasets: [{
		    		label: 'Vendas por mês',
		    		backgroundColor: "rgba(26,179,148,0.5)",
	                pointBorderColor: "rgba(26,179,148,1)",
	                pointBackgroundColor: "#fff",
	                data: valores
		    	}]
		    },
		});
	}
	
	return GraficoVendaPorMes;
	
}());

Brewer.GraficoVendaPorOrigem = (function() {
	function GraficoVendaPorOrigem() {
		this.ctx = $('#graficoVendasPorOrigem')[0].getContext('2d');
	}
	
	GraficoVendaPorOrigem.prototype.iniciar = function() {
		$.ajax({
			url: 'vendas/totalPorOrigem',
			method: 'GET', 
			success: onDadosRecebidos.bind(this)
		});
	}
	
	function onDadosRecebidos(dados) {
		var meses = [];
		var totaisNacional = [];
		var totaisInternacional = [];
		
		dados.forEach(function(obj){
			meses.unshift(obj.mes);
			totaisNacional.unshift(obj.totalNacional);
			totaisInternacional.unshift(obj.totalInternacional);
		});
		
		var graficoVendasPorOrigem = new Chart(this.ctx, {
		    type: 'bar',
		    data: {
		    	labels: meses,
		    	datasets: [{
		    		label: 'Nacional',
		    		backgroundColor: "rgba(220,220,220,0.5)",
	                data: totaisNacional
		    	},
		    	{
		    		label: 'Internacional',
		    		backgroundColor: "rgba(26,179,148,0.5)",
	                data: totaisInternacional
		    	}]
		    },
		});
	}
	
	return GraficoVendaPorOrigem;
	
}());

$(function(){
	
	var graficoVendaPorMes = new Brewer.GraficoVendaPorMes();
	graficoVendaPorMes.iniciar();
	
	var graficoVendaPorOrigem = new Brewer.GraficoVendaPorOrigem();
	graficoVendaPorOrigem.iniciar();
	
});