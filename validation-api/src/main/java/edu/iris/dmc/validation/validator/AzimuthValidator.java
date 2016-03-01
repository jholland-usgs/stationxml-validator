package edu.iris.dmc.validation.validator;

import java.util.Collection;

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
	public boolean isValid(Azimuth value, ConstraintValidatorContext context) {
		if (value.getValue() < this.azimuth.min()) {
			return false;
		}
		if (value.getValue() >= this.azimuth.max()) {System.out.println(value.getValue() +"    "+ this.azimuth.max());
			return false;
		}
		return true;
	}

}
