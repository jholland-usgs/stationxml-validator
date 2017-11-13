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

public class SampleRateDecimationCondition extends AbstractCondition {

	public SampleRateDecimationCondition(boolean required, String description) {
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
		throw new IllegalArgumentException("Not supported!");
	}

	@Override
	public Result evaluate(Channel channel, Response response) {
		
		SampleRate sampleRate = channel.getSampleRate();

		Decimation decimation = null;
		for (ResponseStage stage : response.getStage()) {
			if (stage.getDecimation() != null) {
				decimation = stage.getDecimation();
			}
		}
		if (decimation == null) {
			return Result.of(false,
					"Decimation cannot be null");
		}

		Frequency frequence = decimation.getInputSampleRate();
		BigInteger factor = decimation.getFactor();

		if (frequence == null) {
			return Result.of(false, "frequency is null");
		}

		if (sampleRate.getValue() != (frequence.getValue() / factor.doubleValue())) {
			return Result.of(false, sampleRate.getValue() +"!="+ (frequence.getValue() / factor.doubleValue()));
		}

		return Result.of(true, null);
	}

}
