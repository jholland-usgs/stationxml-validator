package edu.iris.dmc.station.builder;

import java.math.BigInteger;

import edu.iris.dmc.fdsn.station.model.FloatNoUnitType;
import edu.iris.dmc.fdsn.station.model.Frequency;
import edu.iris.dmc.fdsn.station.model.PoleZero;
import edu.iris.dmc.fdsn.station.model.PolesZeros;
import edu.iris.dmc.seed.control.station.B053;
import edu.iris.dmc.seed.control.station.Pole;
import edu.iris.dmc.seed.control.station.Zero;

public class PolesZerosBuilder extends AbstractBuilder {

	public static PolesZeros build(B053 b) throws Exception {
		PolesZeros pzs = factory.createPolesZerosType();
		String transferFunction = null;
		switch (b.getTransferFunctionType()) {
		case 'A':
			transferFunction = "LAPLACE (RADIANS/SECOND)";
			break;
		case 'B':
			transferFunction = "LAPLACE (HERTZ)";
			break;
		case 'D':
			transferFunction = "DIGITAL (Z-TRANSFORM)";
			break;
		}
		if (transferFunction == null) {
			throw new Exception("Invalid blockette 053 transfer function: " + b.getTransferFunctionType());
		}
		pzs.setPzTransferFunctionType(transferFunction);
		pzs.setNormalizationFactor(b.getNormalizationFactor());

		Frequency frequency = factory.createFrequencyType();
		frequency.setValue(b.getNormalizationFrequency());
		pzs.setNormalizationFrequency(frequency);

		int counter = 0;
		if (b.getPoles() != null) {
			for (Zero zero : b.getZeros()) {
				PoleZero z = factory.createPoleZeroType();
				FloatNoUnitType fnt = factory.createFloatNoUnitType();
				fnt.setValue(zero.getReal().getValue());
				z.setReal(fnt);
				
				fnt = factory.createFloatNoUnitType();
				fnt.setValue(zero.getImaginary().getValue());
				z.setImaginary(fnt);

				z.setNumber(BigInteger.valueOf(counter++));
				pzs.getPole().add(z);
			}
			for (Pole pole : b.getPoles()) {
				PoleZero p = factory.createPoleZeroType();
				FloatNoUnitType fnt = factory.createFloatNoUnitType();
				fnt.setValue(pole.getReal().getValue());
				p.setReal(fnt);
				
				fnt = factory.createFloatNoUnitType();
				fnt.setValue(pole.getImaginary().getValue());
				p.setImaginary(fnt);
				p.setNumber(BigInteger.valueOf(counter++));
				pzs.getPole().add(p);
			}

		}
		return pzs;
	}
}
