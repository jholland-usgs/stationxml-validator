package edu.iris.dmc.validation.rule;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import edu.iris.dmc.validation.validator.UnitValidator;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) // on class level
@Constraint(validatedBy = { UnitValidator.class})
@Documented
public @interface Unit {
	
	String message() default "{edu.iris.dmc.validator.rule.Unit}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	boolean required() default false;

	String expression() default "[unassigned]";
}
