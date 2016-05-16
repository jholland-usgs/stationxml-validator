package edu.iris.dmc.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.iris.dmc.fdsn.station.model.Azimuth;

public class AzimuthValidator implements ConstraintValidator<edu.iris.dmc.validation.rule.Azimuth, Azimuth> {

	edu.iris.dmc.validation.rule.Azimuth azimuth;

	@Override
	public void initialize(edu.iris.dmc.validation.rule.Azimuth constraintAnnotation) {
		this.azimuth = constraintAnnotation;

	}

	@Override
	public boolean isValid(Azimuth azimuth, ConstraintValidatorContext context) {
		if (azimuth == null) {
			if (this.azimuth.required()) {
				return false;
			}
			return true;
		}
		if (azimuth.getValue() < this.azimuth.min()) {
			return false;
		}
		if (azimuth.getValue() >= this.azimuth.max()) {
			return false;
		}
		return true;
	}

}
