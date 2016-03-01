package edu.iris.dmc.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.validation.rule.EpochRange;

public class EpochRangeValidator implements ConstraintValidator<EpochRange, Network> {
	private EpochRange constraintAnnotation;

	@Override
	public void initialize(EpochRange constraintAnnotation) {
		this.constraintAnnotation = constraintAnnotation;

	}

	@Override
	public boolean isValid(Network network, ConstraintValidatorContext context) {
		if (network.getStartDate() == null && network.getEndDate() == null) {
			return true;
		}

		if (network.getEndDate() == null) {
			return true;
		}

		if (network.getStartDate() == null) {
			return false;
		}

		if (network.getStartDate().after(network.getEndDate())) {
			return false;
		}
		return true;
	}

}
