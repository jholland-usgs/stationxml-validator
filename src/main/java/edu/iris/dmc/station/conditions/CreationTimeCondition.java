package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class CreationTimeCondition extends AbstractCondition {

	public CreationTimeCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Message evaluate(Network network) {
		throw new IllegalArgumentException("Method not supported for Network.class");
	}

	@Override
	public Message evaluate(Station station) {
		if (station.getCreationDate() == null) {
			return Result.error( "Expected a value between for date but was null.");
		}
		return Result.success();
	}

	@Override
	public Message evaluate(Channel channel) {
		throw new IllegalArgumentException("Method not supported for Channel.class");
	}

}
