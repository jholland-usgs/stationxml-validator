package edu.iris.dmc.validation.validator;

import java.math.BigInteger;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Decimation;
import edu.iris.dmc.fdsn.station.model.Frequency;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
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

		assert(channel != null);

		SampleRate sampleRate = channel.getSampleRate();

		if (sampleRate == null || sampleRate.getValue() == 0) {
			if (channel.getResponse() != null) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("{response.samplerate.405}").addConstraintViolation();
				return false;
			}
		} else {
			Response response = channel.getResponse();
			if (response == null) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("{response.samplerate.406}").addConstraintViolation();
				return false;
			}
			if (response.getInstrumentPolynomial() == null && response.getInstrumentSensitivity() == null) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("{response.samplerate.407}").addConstraintViolation();
				return false;
			}
		}

		if (sampleRate != null) {
			if (channel.getResponse() != null) {
				List<ResponseStage> stages = channel.getResponse().getStage();
				if (stages == null || stages.isEmpty()) {
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate("{response.samplerate.406}").addConstraintViolation();
					return false;
				}
				Decimation decimation = null;
				for (ResponseStage stage : stages) {
					if (stage.getDecimation() != null) {
						decimation = stage.getDecimation();
					}
				}
				if (decimation == null) {
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate("{response.samplerate.408}").addConstraintViolation();
					return false;
				}

				Frequency frequence = decimation.getInputSampleRate();
				BigInteger factor = decimation.getFactor();

				if (frequence == null) {
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate("{response.samplerate.408}").addConstraintViolation();
					return false;
				}

				if (sampleRate.getValue() != (frequence.getValue() / factor.doubleValue())) {
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate("{response.samplerate.408}").addConstraintViolation();
					return false;
				}
			}
		}
		return true;
	}
}
