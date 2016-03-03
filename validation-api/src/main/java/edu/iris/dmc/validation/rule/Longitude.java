package edu.iris.dmc.validation.rule;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import edu.iris.dmc.validation.validator.LongitudeValidator;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) // on class level
@Constraint(validatedBy = { LongitudeValidator.class })
@Documented
public @interface Longitude {
	String message() default "{longitude}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

	double min() default 0;

	double max() default 0;

	boolean required() default false;
}
