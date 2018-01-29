package br.com.luciano.brewer.mail;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import br.com.luciano.brewer.model.Cerveja;
import br.com.luciano.brewer.model.ItemVenda;
import br.com.luciano.brewer.model.Venda;
import br.com.luciano.brewer.storage.FotoStorage;

@Component
public class Mailer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Mailer.class);
	
	@Autowired
	private FotoStorage fotoStorege;
	
	@Autowired		
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine thymeleaf;
	
	@Async
	public void enviar(Venda venda) {
		Context context = new Context();
		Map<String, String> fotos = new HashMap<>();
		boolean adicionarMockCerveja = false;
		
		context.setVariable("venda", venda);
		context.setVariable("logo", "logo");
				
		for(ItemVenda item : venda.getItens()) {
			Cerveja cerveja = item.getCerveja();
			if(cerveja.temFoto()) {
				String cid = "foto-" + cerveja.getId();
				context.setVariable(cid, cid);
				fotos.put(cid, cerveja.getNomeFoto() + "|" + cerveja.getContentType());
			} else {
				adicionarMockCerveja = true;
				context.setVariable("mockCerveja", "mockCerveja");
			}			
		}
		
		try {
			String email = thymeleaf.process("mail/ResumoVenda", context);
			
			MimeMessage mensagem = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");
			helper.setFrom("luclimasilva23@gmail.com");
			helper.setTo(venda.getCliente().getEmail());
			helper.setSubject("Brewer - Venda realizada");
			helper.setText(email, true);
			
			helper.addInline("logo", new ClassPathResource("static/images/logo-gray.png"));
			
			if(adicionarMockCerveja) {
				helper.addInline("mockCerveja", new ClassPathResource("static/images/cerveja-mock.png"));
			}
			
			for (String cid : fotos.keySet()) {
				String [] fotoContentType = fotos.get(cid).split("\\|");
				String foto = fotoContentType[0];
				String contentType = fotoContentType[1];
						
				byte [] arrayFoto = this.fotoStorege.recuperarThumbnail(foto);
				helper.addInline(cid, new ByteArrayResource(arrayFoto), contentType);			
			}
			
			mailSender.send(mensagem);
		} catch (MessagingException e) {
			LOGGER.error("Erro enviando email " + e);
		}
	}

}
