package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Distance;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class DepthCondition extends AbstractCondition {

	public DepthCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Message evaluate(Network network) {
		throw new IllegalArgumentException("Method not supported for Network.class");
	}

	@Override
	public Message evaluate(Station station) {
		throw new IllegalArgumentException("Method not supported for Station.class");
	}

	@Override
	public Message evaluate(Channel channel) {
		if (channel == null) {

		}
		Distance distance = channel.getDepth();
		if (distance == null) {
			return Result.error("depth cannot be null");
		}
		return Result.success();
	}

}
