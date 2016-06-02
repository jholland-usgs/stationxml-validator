package edu.iris.dmc.validation.rule;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import edu.iris.dmc.validation.validator.DistanceValidator;
import edu.iris.dmc.validation.validator.EpochOverlapValidator;
import edu.iris.dmc.validation.validator.GreaterThanValidator;
import edu.iris.dmc.validation.validator.StageSequenceValidator;
import edu.iris.dmc.validation.validator.StageUnitsValidator;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = { StageUnitsValidator.class })
public @interface StageUnits {
	String message() default "{edu.iris.dmc.validator.rule.StageUnits}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	boolean required() default false;
}
