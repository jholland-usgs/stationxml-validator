package edu.iris.dmc.validation.rule;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import edu.iris.dmc.validation.validator.SeedAsciiValidator;
import edu.iris.dmc.validation.validator.SeedFloatingValidator;
import edu.iris.dmc.validation.validator.SeedLatitudeValidator;
import edu.iris.dmc.validation.validator.SeedLongitudeValidator;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) // on class level
@Constraint(validatedBy = { SeedAsciiValidator.class, SeedFloatingValidator.class, SeedLatitudeValidator.class,
		SeedLongitudeValidator.class })
@Documented
public @interface Seed {
	String message() default "{edu.iris.dmc.validator.rule.Seed}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

	int blockette();

	int field();

	int length() default 0;

	double min() default 0;

	double max() default 0;

	boolean required() default false;

	String expression() default "[unassigned]";
}
