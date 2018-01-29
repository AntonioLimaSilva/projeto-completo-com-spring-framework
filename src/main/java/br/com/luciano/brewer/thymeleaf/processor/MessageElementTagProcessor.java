package br.com.luciano.brewer.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class MessageElementTagProcessor extends AbstractElementTagProcessor {
	
	private static final String NOME_TAG = "message";
	private static final short PRECEDENCIA = 1000;
	
	public MessageElementTagProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, NOME_TAG, true, null, false, PRECEDENCIA);
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {
		IModelFactory modelFactoty = context.getModelFactory();
		
		IModel model = modelFactoty.createModel();
		model.add(modelFactoty.createStandaloneElementTag("th:block", "th:replace", "fragments/MensagemSucesso :: alert-success"));
		model.add(modelFactoty.createStandaloneElementTag("th:block", "th:replace", "fragments/MensagensErroValidacao :: alert-danger"));
		
		structureHandler.replaceWith(model, true);
	}

}
