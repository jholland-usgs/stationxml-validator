package edu.iris.dmc.validation.rule;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import edu.iris.dmc.validation.validator.EpochOverlapValidator;
import edu.iris.dmc.validation.validator.EpochRangeValidator;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = { EpochOverlapValidator.class })
public @interface NoOverlap {
	String message() default "{edu.iris.dmc.validator.rule.EpochOverlap}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	boolean required() default false;
	String expression() default "[unassigned]";
}
