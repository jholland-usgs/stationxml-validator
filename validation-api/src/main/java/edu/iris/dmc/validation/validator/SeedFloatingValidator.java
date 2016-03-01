package edu.iris.dmc.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import edu.iris.dmc.validation.rule.Seed;

public class SeedFloatingValidator extends AbstractSeedValidator implements ConstraintValidator<Seed, Double> {

	@Override
	public void initialize(Seed seed) {
		super.initialize(seed);

	}

	@Override
	public boolean isValid(Double value, ConstraintValidatorContext arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
