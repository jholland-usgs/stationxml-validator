package edu.iris.dmc.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.validation.rule.Distance;
import edu.iris.dmc.validation.rule.Elevation;

public class ElevationValidator implements ConstraintValidator<Elevation, Station> {
	private Elevation constraintAnnotation;

	@Override
	public void initialize(Elevation constraintAnnotation) {
		this.constraintAnnotation = constraintAnnotation;

	}

	@Override
	public boolean isValid(Station station, ConstraintValidatorContext context) {
		if (station.getChannels() == null || station.getChannels().isEmpty()) {
			// null collection cannot be validated
			return true;
		}

		for (Channel channel : station.getChannels()) {
			if(channel.getElevation()!=null){
				if(channel.getElevation().getValue()>station.getElevation().getValue()){
					return false;
				}
			}
		}

		return true;
	}

}
