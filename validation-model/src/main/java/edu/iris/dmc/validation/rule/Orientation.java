package edu.iris.dmc.validation.rule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.validation.validator.DecimationValidator;
import edu.iris.dmc.validation.validator.OrientationValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = { OrientationValidator.class })
public @interface Orientation {
	String message() default "{edu.iris.dmc.validator.rule.Orientation}";

	Class<?>[]groups() default {};

	Class<? extends Payload>[]payload() default {};

	boolean required() default false;
}
