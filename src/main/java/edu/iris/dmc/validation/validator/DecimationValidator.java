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

		return true;
	}

	private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == 'K') {
			dist = dist * 1.609344;
		} else if (unit == 'N') {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

}
