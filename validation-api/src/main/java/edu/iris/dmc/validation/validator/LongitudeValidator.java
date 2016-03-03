package edu.iris.dmc.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import edu.iris.dmc.validation.rule.Longitude;

public class LongitudeValidator implements ConstraintValidator<Longitude, edu.iris.dmc.fdsn.station.model.Longitude> {

	private Longitude longitude;

	@Override
	public void initialize(Longitude longitude) {
		this.longitude = longitude;

	}

	@Override
	public boolean isValid(edu.iris.dmc.fdsn.station.model.Longitude value, ConstraintValidatorContext arg1) {
		if (this.longitude.required() && value != null) {
			if (this.longitude.min() <= value.getValue() && this.longitude.max() >= value.getValue()) {
				return true;
			}
		}
		return false;
	}

}
