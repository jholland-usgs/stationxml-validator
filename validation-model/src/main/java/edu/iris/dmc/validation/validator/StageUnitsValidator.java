package edu.iris.dmc.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Units;
import edu.iris.dmc.validation.rule.StageUnits;

public class StageUnitsValidator implements ConstraintValidator<StageUnits, Response> {
	private StageUnits constraintAnnotation;

	@Override
	public void initialize(StageUnits constraintAnnotation) {
		this.constraintAnnotation = constraintAnnotation;
	}

	@Override
	public boolean isValid(Response response, ConstraintValidatorContext context) {
		Units[] current = null;
		for (ResponseStage stage : response.getStage()) {
			Units[] units = stage.getUnits();
			if (units == null) {
				return false;
			}
			if (current != null) {
				if (!current[1].getName().equals(units[0].getName())) {
					return false;
				}
			}
			current = units;
		}
		return true;
	}
}
