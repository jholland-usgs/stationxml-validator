package edu.iris.dmc.station.conditions;

import java.util.List;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Decimation;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.SampleRate;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class SampleRateCondition extends AbstractCondition {

	public SampleRateCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Message evaluate(Network network) {
		throw new IllegalArgumentException("Not supported!");
	}

	@Override
	public Message evaluate(Station station) {
		throw new IllegalArgumentException("Not supported!");
	}

	@Override
	public Message evaluate(Channel channel) {
		SampleRate sampleRate = channel.getSampleRate();
		Response response = channel.getResponse();
		if (sampleRate == null || sampleRate.getValue() == 0) {
			if (response != null) {
				return Result.error("Sample rate cannot be 0 or null.");
			} else {

			}
		} else {
			if (response == null) {
				return Result.error("response cannot be null.");
			} else {
				List<ResponseStage> stages = channel.getResponse().getStage();
				if (stages == null || stages.isEmpty()) {
					return Result.error("Response has no stages");
				}
				Decimation decimation = null;
				for (ResponseStage stage : stages) {
					if (stage.getDecimation() != null) {
						decimation = stage.getDecimation();
						break;
					}
				}
				if (decimation == null) {
					return Result.error("Decimation cannot be null");
				}
			}
			if (response.getInstrumentPolynomial() == null && response.getInstrumentSensitivity() == null) {
				return Result.error("If Channel sample rate > 0, total instrument response must exist as a scale factor or a polynomial.");
			}

		}
		return Result.success();
	}
}
