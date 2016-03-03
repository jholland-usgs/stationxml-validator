package edu.iris.dmc.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.iris.dmc.validation.rule.Latitude;

public class LatitudeValidator implements ConstraintValidator<Latitude, edu.iris.dmc.fdsn.station.model.Latitude> {

	private Latitude latitude;

	@Override
	public void initialize(Latitude latitude) {
		this.latitude = latitude;

	}

	@Override
	public boolean isValid(edu.iris.dmc.fdsn.station.model.Latitude value, ConstraintValidatorContext arg1) {
		if (this.latitude.required() && value != null) {
			if (this.latitude.min() <= value.getValue() && this.latitude.max() >= value.getValue()) {
				return true;
			}
		}
		return false;
	}

}
