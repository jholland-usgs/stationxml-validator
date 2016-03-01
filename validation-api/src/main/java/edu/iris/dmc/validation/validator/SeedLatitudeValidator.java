package edu.iris.dmc.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import edu.iris.dmc.fdsn.station.model.Latitude;
import edu.iris.dmc.validation.rule.Seed;

public class SeedLatitudeValidator extends AbstractSeedValidator implements ConstraintValidator<Seed, Latitude> {

	@Override
	public void initialize(Seed seed) {
		super.initialize(seed);

	}

	@Override
	public boolean isValid(Latitude value, ConstraintValidatorContext arg1) {
		if (this.seed.required() && value != null) {
			if (this.seed.min() <= value.getValue() && this.seed.max()>=value.getValue()) {
				return true;
			}
		}
		return false;
	}

}
