package edu.iris.dmc.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.iris.dmc.fdsn.station.model.Azimuth;
import edu.iris.dmc.fdsn.station.model.Dip;

public class DipValidator implements ConstraintValidator<edu.iris.dmc.validation.rule.Dip, Dip> {

	edu.iris.dmc.validation.rule.Dip dip;

	@Override
	public void initialize(edu.iris.dmc.validation.rule.Dip constraintAnnotation) {
		this.dip = constraintAnnotation;

	}

	@Override
	public boolean isValid(Dip dip, ConstraintValidatorContext context) {
		if (dip == null) {
			if (this.dip.required()) {
				return false;
			}
			return true;
		}
		if (dip.getValue() < this.dip.min()) {
			return false;
		}
		if (dip.getValue() >= this.dip.max()) {
			return false;
		}
		return true;
	}

}
