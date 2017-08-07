package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Dip;
import edu.iris.dmc.fdsn.station.model.Equipment;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Result;

public class SensorCondition extends AbstractCondition {

	public SensorCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Result evaluate(Network network) {
		throw new IllegalArgumentException("method not supported for network.");
	}

	@Override
	public Result evaluate(Station station) {
		throw new IllegalArgumentException("method not supported for station.");
	}

	@Override
	public Result evaluate(Channel channel) {
		if (channel == null) {
			return Result.of(true, null);
		}

		Equipment equipment = channel.getSensor();
		if (equipment == null) {
			return Result.of(false, "expected equipment/sensor but was null");
		} else {
			if (equipment.getDescription() == null) {
				return Result.of(false, "expected equipment/sensor description but was null");
			}
		}
		return Result.of(true, null);
	}

}
