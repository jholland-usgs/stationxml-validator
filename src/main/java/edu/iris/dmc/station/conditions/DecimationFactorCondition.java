package edu.iris.dmc.station.conditions;

import java.util.List;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class DecimationFactorCondition extends AbstractCondition {

	public DecimationFactorCondition(boolean required, String description) {
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
	public Message evaluate(Channel channel, Response response) {
		List<ResponseStage> stages = response.getStage();
		if (stages == null || stages.isEmpty()) {
			Result.success();
		}

		int i = 1;
		for (ResponseStage stage : stages) {
			if (stage.getDecimation() != null && stage.getDecimation().getFactor() != null) {
				int factor = stage.getDecimation().getFactor().intValue();
				if (factor > 1) {
					if (stage.getDecimation().getCorrection() == null) {
						return Result.error("Decimation correction is null for stage number: " + i);
					}
					if (stage.getDecimation().getCorrection().getValue() != 0) {

					} else {
						return Result.error("Decimation correction is null for stage number: " + i);
					}
				}
			}
			i++;
		}

		return Result.success();
	}
}
