package edu.iris.dmc.station.conditions;

import java.util.List;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Result;

public class MissingDecimationCondition extends AbstractCondition {

	public MissingDecimationCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Result evaluate(Network network) {
		throw new IllegalArgumentException("method not supported!");
	}

	@Override
	public Result evaluate(Station station) {
		throw new IllegalArgumentException("method not supported!");
	}

	@Override
	public Result evaluate(Channel channel) {
		throw new IllegalArgumentException("method not supported!");
	}

	@Override
	public Result evaluate(Response response) {
		List<ResponseStage> stages = response.getStage();
		if (stages == null || stages.isEmpty()) {
			Result.of(true, null);
		}

		int i = 1;
		for (ResponseStage stage : stages) {
			if (stage.getFIR() != null || stage.getCoefficients() != null) {
				if (stage.getDecimation() == null) {
					return Result.of(false, "stage number: " + i);
				}
			}
			if (stage.getPolesZeros() != null) {
				if ("DIGITAL (Z-TRANSFORM)".equals(stage.getPolesZeros().getPzTransferFunctionType())) {
					if (stage.getDecimation() == null) {
						return Result.of(false, "stage number: " + i);
					}
				}
			}
			i++;
		}
		return Result.of(true, null);
	}
}
