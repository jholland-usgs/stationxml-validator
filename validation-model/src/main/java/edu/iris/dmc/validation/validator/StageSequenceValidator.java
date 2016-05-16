package edu.iris.dmc.validation.validator;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.validation.rule.StageSequence;


public class StageSequenceValidator implements ConstraintValidator<StageSequence, Response> {
	private StageSequence constraintAnnotation;

	@Override
	public void initialize(StageSequence constraintAnnotation) {
		this.constraintAnnotation = constraintAnnotation;

	}

	@Override
	public boolean isValid(Response response, ConstraintValidatorContext context) {
		List<ResponseStage> stages = response.getStage();
		if (stages == null || stages.isEmpty()) {
			return true;
		}
		int i = 1;
		for (ResponseStage stage : stages) {
			if (stage.getNumber() == null) {
				return false;
			}
			if (i != stage.getNumber().intValue()) {
				return false;
			}
			i++;
		}
		return true;
	}

}
