package edu.iris.dmc.validation.validator;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.iris.dmc.fdsn.station.model.Decimation;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Units;
import edu.iris.dmc.validation.rule.Stage;

public class StageValidator implements ConstraintValidator<Stage, Response> {
	private Stage constraintAnnotation;

	@Override
	public void initialize(Stage constraintAnnotation) {
		this.constraintAnnotation = constraintAnnotation;

	}

	@Override
	public boolean isValid(Response response, ConstraintValidatorContext context) {
		List<ResponseStage> stages = response.getStage();
		if (stages == null || stages.isEmpty()) {
			return true;
		}
		int i = 1;
		Double inputSampleRateByFactor = null;
		Units[] current = null;

		for (ResponseStage stage : stages) {

				if (stage.getNumber() == null || i != stage.getNumber().intValue()) {
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate("{response.stage.sequence}").addConstraintViolation();
					return false;
				}

				Units[] units = stage.getUnits();
				if (units == null) {
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate("{response.stage.unit}").addConstraintViolation();
				} else {
					if (current != null) {

						if (!current[1].getName().equals(units[0].getName())) {
							context.disableDefaultConstraintViolation();
							context.buildConstraintViolationWithTemplate("{response.stage.unit}")
									.addConstraintViolation();

							return false;
						}
					}
				}
				current = units;


			Decimation decimation = stage.getDecimation();
			if (stage.getDecimation() != null) {
				double inputSampleRate = decimation.getInputSampleRate().getValue();
				if (inputSampleRateByFactor != null) {
					if (inputSampleRate != inputSampleRateByFactor.doubleValue()) {
						context.disableDefaultConstraintViolation();
						context.buildConstraintViolationWithTemplate("{response.stage.decimation.414}")
								.addConstraintViolation();
						return false;
					}
				}
				inputSampleRateByFactor = inputSampleRate / decimation.getFactor().longValueExact();
			}

			i++;
		}
		return true;
	}

}
