package edu.iris.dmc.station.builder;

import java.math.BigInteger;

import edu.iris.dmc.fdsn.station.model.Decimation;
import edu.iris.dmc.fdsn.station.model.Float;
import edu.iris.dmc.fdsn.station.model.Frequency;
import edu.iris.dmc.seed.control.station.B057;

public class DecimationBuilder extends AbstractBuilder {

	public static Decimation build(B057 b) {
		Decimation decimation = factory.createDecimationType();

		Float ft = factory.createFloatType();
		ft.setValue(b.getCorrection());
		decimation.setCorrection(ft);

		ft = factory.createFloatType();
		ft.setValue(b.getEstimatedDelay());
		decimation.setDelay(ft);

		decimation.setOffset(BigInteger.valueOf(b.getDecimationOffset()));

		decimation.setFactor(BigInteger.valueOf(b.getDecimationFactor()));

		Frequency fType = factory.createFrequencyType();
		fType.setValue(b.getSampleRate());
		decimation.setInputSampleRate(fType);

		return decimation;
	}
}
