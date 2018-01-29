package br.com.luciano.brewer.service;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.luciano.brewer.service.exception.NegocioException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class RelatorioVendaService {
	
	@Autowired
	private DataSource dataSource;
	
	public byte[] gerarRelatorioVendaComItens(Integer id) throws Exception {
		Connection connection = null;
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("format", "pdf");
		parametros.put("id_venda", id);
		
		InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/venda/venda.jasper");
		
		try {
			connection = this.dataSource.getConnection();
			JasperPrint jasper = JasperFillManager.fillReport(inputStream, parametros, connection);
			
			return JasperExportManager.exportReportToPdf(jasper);
		} catch (Exception e) {
			throw new NegocioException("Erro gerando relatório");
		} finally {		
			connection.close();
		}
		
	}

}
