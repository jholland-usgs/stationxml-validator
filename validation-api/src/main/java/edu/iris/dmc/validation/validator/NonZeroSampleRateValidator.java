package edu.iris.dmc.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.SampleRate;
import edu.iris.dmc.validation.rule.NonZeroSampleRate;


public class NonZeroSampleRateValidator implements ConstraintValidator<NonZeroSampleRate, Channel> {
	private NonZeroSampleRate constraintAnnotation;

	@Override
	public void initialize(NonZeroSampleRate constraintAnnotation) {
		this.constraintAnnotation = constraintAnnotation;

	}

	@Override
	public boolean isValid(Channel channel, ConstraintValidatorContext context) {

		SampleRate sampleRate = channel.getSampleRate();
		if (sampleRate == null || sampleRate.getValue() == 0) {
			if (channel.getResponse() != null) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(
						"If the Channel sample rate is 0 (non-timeseries ASCII channel), no Response should be included.")
						.addConstraintViolation();
				return false;
			}
		} else {
			Response response = channel.getResponse();
			if (response == null || response.getStage() == null || response.getStage().isEmpty()) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(
						"If the Channel sample rate is nonzero, at least one stage must be included (includes units, gain and sample rate) ..")
						.addConstraintViolation();
				return false;
			}
			response.getInstrumentPolynomial();
			response.getInstrumentSensitivity();
		}
		return true;
	}
}
