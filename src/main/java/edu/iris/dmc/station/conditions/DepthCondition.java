package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Distance;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Result;

public class DepthCondition extends AbstractCondition {

	public DepthCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Result evaluate(Network network) {
		throw new IllegalArgumentException("Method not supported for Network.class");
	}

	@Override
	public Result evaluate(Station station) {
		throw new IllegalArgumentException("Method not supported for Station.class");
	}

	@Override
	public Result evaluate(Channel channel) {
		if (channel == null) {

		}
		Distance distance = channel.getDepth();
		if (distance == null) {
			return Result.of(false, "depth cannot be null");
		}
		return Result.of(true, null);
	}

}
