package edu.iris.dmc.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.iris.dmc.fdsn.station.model.BaseNodeType;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Latitude;
import edu.iris.dmc.fdsn.station.model.Longitude;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.validation.rule.GeoLocation;

public class GeoLocationValidator implements ConstraintValidator<GeoLocation, BaseNodeType> {
	private GeoLocation constraintAnnotation;

	@Override
	public void initialize(GeoLocation constraintAnnotation) {
		this.constraintAnnotation = constraintAnnotation;

	}

	@Override
	public boolean isValid(BaseNodeType node, ConstraintValidatorContext context) {
		if(node instanceof Station){
			Station station = (Station)node;
			Latitude latitude = station.getLatitude();
			Longitude longtiude = station.getLongitude();
			if(latitude==null || longtiude==null){
				return true;
			}
			if(latitude.getValue()==0 && longtiude.getValue()==0){
				
			}
		}else if(node instanceof Channel){
			Channel channel = (Channel)node;
			Latitude latitude = channel.getLatitude();
			Longitude longtiude = channel.getLongitude();
			if(latitude==null || longtiude==null){
				return true;
			}
			if(latitude.getValue()==0 && longtiude.getValue()==0){
				
			}
		}else{
			
		}


		return true;
	}

}
