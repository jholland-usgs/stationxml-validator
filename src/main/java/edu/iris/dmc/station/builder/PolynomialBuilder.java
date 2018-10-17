package edu.iris.dmc.station.builder;

import java.math.BigDecimal;
import java.math.BigInteger;

import edu.iris.dmc.fdsn.station.model.Coefficient;
import edu.iris.dmc.fdsn.station.model.Frequency;
import edu.iris.dmc.fdsn.station.model.Polynomial;
import edu.iris.dmc.seed.control.station.B062;

public class PolynomialBuilder extends AbstractBuilder {

	public static Polynomial build(B062 b) {
		Polynomial pType = factory.createPolynomialType();

		if ("M".equals(pType.getApproximationType())) {
			pType.setApproximationType("MACLAURIN");
		}
		if (b.getLowerBoundOfApproximation() != null) {
			pType.setApproximationLowerBound(BigDecimal.valueOf(b.getLowerBoundOfApproximation()));
		}

		if (b.getUpperBoundOfApproximation() != null) {
			pType.setApproximationUpperBound(BigDecimal.valueOf(b.getUpperBoundOfApproximation()));
		}

		if (b.getLowerValidFrequencyBound() != null) {
			Frequency ft = factory.createFrequencyType();
			ft.setValue(b.getLowerValidFrequencyBound());
			pType.setFrequencyLowerBound(ft);
		}

		if (b.getUpperValidFrequencyBound() != null) {
			Frequency ft = factory.createFrequencyType();
			ft.setValue(b.getUpperValidFrequencyBound());
			pType.setFrequencyUpperBound(ft);
		}

		if (b.getMaximumAbsoluteError() != null) {
			pType.setMaximumError(BigDecimal.valueOf(b.getMaximumAbsoluteError()));
		}

		int index = 1;
		for (edu.iris.dmc.seed.control.station.Number n : b.getCoefficients()) {
			n.getValue();
			n.getError();
			Coefficient co = factory.createPolynomialTypeCoefficient();
			co.setNumber(BigInteger.valueOf(index));

			co.setMinusError(n.getError());
			co.setPlusError(n.getError());
			co.setValue(n.getValue());
			pType.getCoefficient().add(co);
			index++;
		}
		return pType;
	}
}
