package edu.iris.dmc.validation.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class NonZeroValidator implements ConstraintValidator<edu.iris.dmc.validation.rule.NonZero, Double> {

	edu.iris.dmc.validation.rule.NonZero rule;

	@Override
	public void initialize(edu.iris.dmc.validation.rule.NonZero constraintAnnotation) {
		this.rule = constraintAnnotation;

	}

	@Override
	public boolean isValid(Double value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}

		if (Math.abs(value) > 0) {
			return true;
		}
		return false;
	}

}
