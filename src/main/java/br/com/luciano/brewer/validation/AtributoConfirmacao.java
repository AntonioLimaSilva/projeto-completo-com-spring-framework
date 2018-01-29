package br.com.luciano.brewer.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

import br.com.luciano.brewer.validation.validator.AtributoConfirmacaoValidator;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtributoConfirmacaoValidator.class)
public @interface AtributoConfirmacao {
	
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

	@OverridesAttribute(constraint = Pattern.class, name = "message")
	String message() default "Atributos não conferem!";
	
	String atributo();
	
	String atributoConfirmacao();
}
