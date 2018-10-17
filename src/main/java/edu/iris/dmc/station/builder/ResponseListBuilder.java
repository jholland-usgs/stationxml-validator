package edu.iris.dmc.station.builder;

import edu.iris.dmc.fdsn.station.model.AngleType;
import edu.iris.dmc.fdsn.station.model.Frequency;
import edu.iris.dmc.fdsn.station.model.ObjectFactory;
import edu.iris.dmc.fdsn.station.model.ResponseList;
import edu.iris.dmc.fdsn.station.model.ResponseListElement;
import edu.iris.dmc.seed.control.station.B055;

public class ResponseListBuilder extends AbstractBuilder {

	public static ResponseList build(B055 b) {

		ResponseList rType = new ObjectFactory().createResponseListType();

		for (edu.iris.dmc.seed.control.station.B055.Response response : b.getResponses()) {
			ResponseListElement rlet = factory.createResponseListElementType();
			rlet.setFrequency(rlet.getFrequency());

			if (response.getFrequency() != null) {
				Frequency ft = factory.createFrequencyType();
				ft.setValue(response.getFrequency());
				rlet.setFrequency(rlet.getFrequency());
			}

			if (response.getAmplitude() != null) {
				Frequency ft = factory.createFrequencyType();
				ft.setValue(response.getAmplitude());
				ft.setMinusError(response.getAmplitudeError());
				ft.setPlusError(response.getAmplitudeError());
				rlet.setAmplitude(ft);
			}

			if (response.getPhaaeAngle() != null) {
				AngleType at = factory.createAngleType();
				at.setValue(response.getPhaaeAngle());
				at.setMinusError(response.getPhaseError());
				at.setPlusError(response.getPhaseError());
				rlet.setPhase(at);
			}

			rType.getResponseListElement().add(rlet);
		}
		return rType;
	}
}
