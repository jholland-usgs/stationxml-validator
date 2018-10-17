package edu.iris.dmc.station.builder;

import edu.iris.dmc.fdsn.station.model.Longitude;

public class LongitudeBuilder extends AbstractBuilder{

	public static Longitude build(Double latitude) throws Exception {
		if (latitude == null) {
			return null;
		}

		Longitude l = factory.createLongitudeType();
		l.setValue(latitude);
		l.setDatum("WGS84");
		// l.setMinusError(value);
		// l.setPlusError(value);
		l.setUnit("DEGREES");
		return l;
	}
}
