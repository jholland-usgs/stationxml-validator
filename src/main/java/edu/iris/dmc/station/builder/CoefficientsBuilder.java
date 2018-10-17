package edu.iris.dmc.station.builder;

import edu.iris.dmc.fdsn.station.model.Coefficients;
import edu.iris.dmc.fdsn.station.model.Float;
import edu.iris.dmc.fdsn.station.model.ObjectFactory;
import edu.iris.dmc.seed.control.station.B054;

public class CoefficientsBuilder extends AbstractBuilder {

	public static Coefficients build(B054 b) {

		Coefficients coefficients = new ObjectFactory().createCoefficientsType();

		char responseType = b.getResponseType();
		String transferFunction = "";
		if ('A' == responseType) {
			transferFunction = "ANALOG (RADIANS/SECOND)";
		} else if ('B' == responseType) {
			transferFunction = "ANALOG (HERTZ)";
		} else if ('D' == responseType) {
			transferFunction = "DIGITAL";
		}

		coefficients.setCfTransferFunctionType(transferFunction);
		for (edu.iris.dmc.seed.control.station.Number n : b.getNumerators()) {
			Float ft = new ObjectFactory().createFloatType();
			ft.setValue(n.getValue());
			ft.setMinusError(n.getError());
			ft.setPlusError(n.getError());
			coefficients.getNumerator().add(ft);
		}
		for (edu.iris.dmc.seed.control.station.Number n : b.getDenominators()) {
			Float ft = new ObjectFactory().createFloatType();
			ft.setValue(n.getValue());
			ft.setMinusError(n.getError());
			ft.setPlusError(n.getError());
			coefficients.getDenominator().add(ft);
		}

		return coefficients;

	}
}
