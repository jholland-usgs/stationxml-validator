package edu.iris.dmc.validation.rule;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import edu.iris.dmc.validation.validator.EpochOverlapValidator;
import edu.iris.dmc.validation.validator.GreaterThanValidator;
import edu.iris.dmc.validation.validator.NonZeroSampleRateValidator;
import edu.iris.dmc.validation.validator.NonZeroValidator;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = { NonZeroValidator.class })
public @interface NonZero {
	String message() default "{nonZero}";

	Class<?>[]groups() default {};

	Class<? extends Payload>[]payload() default {};

	boolean required() default false;

	String expression() default "[unassigned]";
}
