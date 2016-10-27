package edu.iris.dmc.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.validation.rule.Orientation;

public class OrientationValidator implements ConstraintValidator<Orientation, Channel> {
	private Orientation constraintAnnotation;

	@Override
	public void initialize(Orientation constraintAnnotation) {
		this.constraintAnnotation = constraintAnnotation;

	}

	@Override
	public boolean isValid(Channel channel, ConstraintValidatorContext context) {

		assert(channel != null);

		if (channel.getCode() == null) {
			// if this is an error, it should be caught somewhere else
			return true;
		}

		if (channel.getCode().trim().length() < 3) {
			// if this is an error, it should be caught somewhere else
			return true;
		}

		char[] array = channel.getCode().toCharArray();

		double azimuth = channel.getAzimuth().getValue();
		double dip = channel.getDip().getValue();

		boolean valid = true;
		StringBuilder messageBuilder = new StringBuilder();
		if ('E' == array[2]) {
			if (azimuth > 95 && azimuth < 85) {
				valid = false;
				messageBuilder.append("azimuth: ").append(azimuth).append(" ");
			}

			if (dip > 5 || dip < -5) {
				valid = false;
				messageBuilder.append("dip: ").append(dip).append(" ");
			}
		} else if ('N' == array[2]) {
			if (azimuth > 5 && azimuth < 355) {
				valid = false;
				messageBuilder.append("azimuth: ").append(azimuth).append(" ");
			}
			if (dip > 5 || dip < -5) {
				valid = false;
				messageBuilder.append("dip: ").append(dip).append(" ");
			}
		} else if ('Z' == array[2]) {
			if (azimuth > 5 && azimuth < 355) {
				valid = false;
				messageBuilder.append("azimuth: ").append(azimuth).append(" ");
			}
			if (dip > -85) {
				valid = false;
				messageBuilder.append("dip: ").append(dip).append(" ");
			}
		}

		if (valid) {
			return true;
		}
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(
				"315,Invalid channel orientation: " + messageBuilder.toString() + " for " + channel.getCode()).addConstraintViolation();
		return false;
	}
}
