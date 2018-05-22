package edu.iris.dmc.station.conditions;

import java.util.List;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class MissingDecimationCondition extends AbstractCondition {

	public MissingDecimationCondition(boolean required, String description) {
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
			return Result.success();
		}

		int i = 1;
		for (ResponseStage stage : stages) {
			if (stage.getFIR() != null || stage.getCoefficients() != null) {
				if (stage.getDecimation() == null) {
					return Result.error( "stage number: " + i);
				}
			}
			if (stage.getPolesZeros() != null) {
				if ("DIGITAL (Z-TRANSFORM)".equals(stage.getPolesZeros().getPzTransferFunctionType())) {
					if (stage.getDecimation() == null) {
						return Result.error( "stage number: " + i);
					}
				}
			}
			i++;
		}
		return Result.success();
	}
}
