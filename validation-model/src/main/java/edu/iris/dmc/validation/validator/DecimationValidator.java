package edu.iris.dmc.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.validation.rule.MissingDecimation;

public class DecimationValidator implements ConstraintValidator<MissingDecimation, ResponseStage> {
	private MissingDecimation constraintAnnotation;

	@Override
	public void initialize(MissingDecimation constraintAnnotation) {
		this.constraintAnnotation = constraintAnnotation;

	}

	@Override
	public boolean isValid(ResponseStage stage, ConstraintValidatorContext context) {

		if (stage.getFIR() != null || stage.getCoefficients() != null) {
			if (stage.getDecimation() == null) {
				return false;
			}
		}
		if (stage.getPolesZeros() != null) {
			if ("DIGITAL (Z-TRANSFORM)".equals(stage.getPolesZeros().getPzTransferFunctionType())) {
				if (stage.getDecimation() == null) {
					return false;
				}
			}
		}

		if (stage.getDecimation() != null && stage.getDecimation().getFactor() != null) {
			int factor = stage.getDecimation().getFactor().intValue();
			if (factor > 1) {
				if (stage.getDecimation().getCorrection() == null) {
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate("{response.stage.decimation.413}")
							.addConstraintViolation();
				}
				if (stage.getDecimation().getCorrection().getValue() != 0) {

				} else {
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate("{response.stage.decimation.413}")
							.addConstraintViolation();
					return false;
				}

			}
		}

		return true;
	}

}
