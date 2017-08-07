package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Result;

public class StartTimeCondition extends AbstractCondition {

	public StartTimeCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Result evaluate(Network network) {
		if (network.getStartDate() == null) {
			return Result.of(false, "startDate is null");
		}
		return Result.of(true, null);
	}

	@Override
	public Result evaluate(Station station) {
		if (station.getStartDate() == null) {
			return Result.of(false, "startDate is null");
		}
		return Result.of(true, null);
	}

	@Override
	public Result evaluate(Channel channel) {
		if (channel.getStartDate() == null) {
			return Result.of(false, "startDate is null");
		}
		return Result.of(true, null);
	}

}
