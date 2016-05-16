package edu.iris.dmc.validation.rule;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import edu.iris.dmc.validation.validator.AzimuthValidator;
import edu.iris.dmc.validation.validator.EpochOverlapValidator;
import edu.iris.dmc.validation.validator.EpochRangeValidator;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = { AzimuthValidator.class })
public @interface Azimuth {
	String message() default "{edu.iris.dmc.validator.rule.Azimuth}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	boolean required() default false;
	double min();
	double max();
}
