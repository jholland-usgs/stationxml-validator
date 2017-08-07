package edu.iris.dmc.station.conditions;

import java.util.List;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Result;

public class StageSequenceCondition extends AbstractCondition {

	public StageSequenceCondition(boolean required, String description) {
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
		if (this.required) {
			if (response == null) {
				return Result.of(false, "expected response but was null");
			}
		}
		if (response.getStage() != null && !response.getStage().isEmpty()) {
			List<ResponseStage> stages = response.getStage();
			ResponseStage stage = stages.get(stages.size() - 1);
			if (stage.getNumber().intValue() == stages.size()-1) {
				return Result.of(false, "invalida sequence number " + stage.getNumber().intValue());
			} else {
				int i = 1;
				for (ResponseStage s : stages) {
					if (s.getNumber().intValue() != i) {
						return Result.of(false,
								"invalida sequence number " + s.getNumber().intValue() + " expected: " + i);
					}
					i++;
				}
			}
		}
		return Result.of(true, null);
	}
}
