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
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class DecimationCondition extends ChannelRestrictedCondition {

	public DecimationCondition(boolean required, String description, Restriction... restrictions) {
		super(required, description, restrictions);
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
		if(channel==null){
			throw new IllegalArgumentException("Channel cannot be null.");
		}
		return evaluate(channel,channel.getResponse());
	}

	// The value of Channel::SampleRate must be equal to the value of
	// Decimation::InputSampleRate divided by Decimation::Factor of the final
	// response stage.

	@Override
	public Message evaluate(Channel channel, Response response) {
		if (isRestricted(channel)) {
			return Result.success();
		}
		List<ResponseStage> stages = response.getStage();
		if (stages == null || stages.isEmpty()) {
			Result.success();
		}
		Double inputSampleRateByFactor = null;
		int i = 1;
		for (ResponseStage stage : stages) {
			Decimation decimation = stage.getDecimation();
			if (stage.getDecimation() != null) {
				double inputSampleRate = decimation.getInputSampleRate().getValue();
				if (inputSampleRateByFactor != null) {
					if (Math.abs(inputSampleRate - inputSampleRateByFactor.doubleValue()) > 0.001) {
						return Result.error("stage number: " + i+" inputSampleRate="+inputSampleRate+" : inputSampleRateByFactor="+inputSampleRateByFactor.doubleValue());
					}
				}
				inputSampleRateByFactor = inputSampleRate / decimation.getFactor().doubleValue();
			}
			i++;
		}

		return Result.success();
	}

}
