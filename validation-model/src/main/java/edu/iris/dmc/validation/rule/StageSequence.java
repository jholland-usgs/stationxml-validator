package edu.iris.dmc.validation.rule;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import edu.iris.dmc.validation.validator.DistanceValidator;
import edu.iris.dmc.validation.validator.EpochOverlapValidator;
import edu.iris.dmc.validation.validator.EpochRangeValidator;
import edu.iris.dmc.validation.validator.StageSequenceValidator;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = { StageSequenceValidator.class })
public @interface StageSequence {
	String message() default "{edu.iris.dmc.validator.rule.StageSequence}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	boolean required() default false;
}
