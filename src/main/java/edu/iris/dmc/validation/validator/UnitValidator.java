package edu.iris.dmc.validation.validator;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.iris.dmc.fdsn.station.model.Units;
import edu.iris.dmc.validation.rule.Unit;


public class UnitValidator implements ConstraintValidator<Unit, Units> {

	private Unit unit;
	private String[] units = new String[] { "C", "NULL", "CELSIUS", "RAD", "COUNTS/(CM/SEC2)", "Pa", "MS", "COUNTS/MV",
			"KPA", "NM/S^-2", "MB", "M/S", "AMPERES", "MILLIBAR", "M/S**2", "HECTOPASCALS", "USEC", "NANORADIANS",
			"NM/SEC", "PASCALS", "MM/HOUR", "M/M", "COUNTS", "RAD/S**2", "NANOTESLA", "COUNTS", "D", "M/S", "MM", "HZ",
			"CALIBRATION", "NANOSTRAIN", "VOLTS", "KILOPASCALS", "NONE", "PA", "S", "RAD/SEC", "NM", "MILLIBARS",
			"MINUTES", "DEGREES", "NONE.SPECIFIED", "PERCENT", "COUNTS", "NM/S**2", "U", "NM/S", "NT", "HPA", "TILT",
			"V", "COUNTS/V", "MBAR", "RADIANS", "A", "M/S", "GAPS", "URADIAN", "CM/S", "UNKNOWN", "RAD/S", "SEC",
			"MICRORADIANS", "MICROSTRAIN" };

	@Override
	public void initialize(Unit unit) {
		this.unit = unit;
	}

	@Override
	public boolean isValid(Units units, ConstraintValidatorContext context) {
		if (unit.required()) {
			if (units == null || units.getName() == null) {
				return false;
			}

			return Arrays.asList(this.units).contains(units.getName());
		}
		return true;
	}

}
