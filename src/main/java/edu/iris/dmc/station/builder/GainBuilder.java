package edu.iris.dmc.station.builder;

import edu.iris.dmc.fdsn.station.model.Gain;
import edu.iris.dmc.seed.control.station.B058;
import edu.iris.dmc.seed.control.station.Calibration;

public class GainBuilder extends AbstractBuilder {

	public static Gain build(B058 b) {
		Gain gainType = factory.createGainType();
		gainType.setValue(b.getSensitivity());
		gainType.setFrequency(b.getFrequency());
		for(Calibration c:b.getHistory()) {
			c.getFrequency();
			c.getSensitivity();
			c.getTime();
		}
		return gainType;
	}
}
