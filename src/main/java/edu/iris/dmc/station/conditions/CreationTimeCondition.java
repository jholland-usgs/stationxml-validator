package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Result;

public class CreationTimeCondition extends AbstractCondition {

	public CreationTimeCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Result evaluate(Network network) {
		throw new IllegalArgumentException("Method not supported for Network.class");
	}

	@Override
	public Result evaluate(Station station) {
		if (station.getCreationDate() == null) {
			return Result.of(false, "Expected a value between for date but was null.");
		}
		return Result.of(true, "");
	}

	@Override
	public Result evaluate(Channel channel) {
		throw new IllegalArgumentException("Method not supported for Channel.class");
	}

}
