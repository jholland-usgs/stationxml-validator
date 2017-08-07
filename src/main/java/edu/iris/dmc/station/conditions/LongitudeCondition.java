package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Longitude;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Result;

public class LongitudeCondition extends AbstractCondition {
	public LongitudeCondition(boolean required, String description) {
		super(required, description);
	}

	private Result run(Longitude longitude) {
		if (longitude == null) {
			return Result.of(false, "longitude cannot be null");
		}
		if (-180 <= longitude.getValue() && 180 >= longitude.getValue()) {
			return Result.of(true, null);
		}
		return Result.of(false, "Expected a value between -180 and 180 but was " + longitude.getValue());
	}

	@Override
	public String toString() {
		return "CodeRule [description=" + description + ", required=" + required + " ]";
	}

	@Override
	public Result evaluate(Network network) {
		throw new IllegalArgumentException("Not supported for network.");
	}

	@Override
	public Result evaluate(Station station) {
		return run(station.getLongitude());
	}

	@Override
	public Result evaluate(Channel channel) {
		return run(channel.getLongitude());
	}

}
