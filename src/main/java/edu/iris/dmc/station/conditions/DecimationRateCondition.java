package edu.iris.dmc.station.conditions;

import java.util.List;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Decimation;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class DecimationRateCondition extends AbstractCondition {

	public DecimationRateCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Message evaluate(Network network) {
		throw new IllegalArgumentException("method not supported!");
	}

	@Override
	public Message evaluate(Station station) {
		throw new IllegalArgumentException("method not supported!");
	}

	@Override
	public Message evaluate(Channel channel) {
		throw new IllegalArgumentException("method not supported!");
	}

	@Override
	public Message evaluate(Channel channel,Response response) {
		List<ResponseStage> stages = response.getStage();
		if (stages == null || stages.isEmpty()) {
			Result.success();
		}
		Double inputSampleRateByFactor = null;
		int i=1;
		for (ResponseStage stage : stages) {
			Decimation decimation = stage.getDecimation();
			if (stage.getDecimation() != null) {
				double inputSampleRate = decimation.getInputSampleRate().getValue();
				if (inputSampleRateByFactor != null) {
					if (inputSampleRate != inputSampleRateByFactor.doubleValue()) {
						return Result.error( "stage number: "+i);
					}
				}
				inputSampleRateByFactor = inputSampleRate / decimation.getFactor().longValueExact();
			}
			i++;
		}

		return Result.success();
	}
}
