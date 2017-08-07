package edu.iris.dmc.station.conditions;

import java.math.BigInteger;
import java.util.List;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Decimation;
import edu.iris.dmc.fdsn.station.model.Frequency;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.SampleRate;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Result;

public class SampleRateCondition extends AbstractCondition {

	public SampleRateCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Result evaluate(Network network) {
		throw new IllegalArgumentException("Not supported!");
	}

	@Override
	public Result evaluate(Station station) {
		throw new IllegalArgumentException("Not supported!");
	}

	@Override
	public Result evaluate(Channel channel) {
		SampleRate sampleRate = channel.getSampleRate();
		Response response = channel.getResponse();
		if (sampleRate == null || sampleRate.getValue() == 0) {
			if (response != null) {
				return Result.of(false, "");
			} else {

			}
		} else {
			if (response == null) {
				return Result.of(false,
						"If Channel sample rate > 0, at least one stage must be included and be comprised of units, gain, and sample rate.");
			} else {
				List<ResponseStage> stages = channel.getResponse().getStage();
				if (stages == null || stages.isEmpty()) {
					return Result.of(false,
							"If Channel sample rate > 0, at least one stage must be included and be comprised of units, gain, and sample rate.");
				}
				Decimation decimation = null;
				for (ResponseStage stage : stages) {
					if (stage.getDecimation() != null) {
						decimation = stage.getDecimation();
					}
				}
				if (decimation == null) {
					return Result.of(false,
							"The value of Channel::SampleRate must be equal to the value of Decimation::InputSampleRate divided by Decimation::Factor of the final response stage.");
				}

				Frequency frequence = decimation.getInputSampleRate();
				BigInteger factor = decimation.getFactor();

				if (frequence == null) {
					return Result.of(false, "408");
				}

				if (sampleRate.getValue() != (frequence.getValue() / factor.doubleValue())) {
					return Result.of(false, "408");
				}
			}
			if (response.getInstrumentPolynomial() == null && response.getInstrumentSensitivity() == null) {
				return Result.of(false,
						"If Channel sample rate > 0, total instrument response must exist as either or.");
			}

		}
		return Result.of(true, null);
	}
}
