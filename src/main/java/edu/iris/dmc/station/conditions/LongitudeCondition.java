package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Longitude;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class LongitudeCondition extends AbstractCondition {
	public LongitudeCondition(boolean required, String description) {
		super(required, description);
	}

	private Message run(Longitude longitude) {
		if (longitude == null) {
			return Result.error("longitude cannot be null");
		}
		if (longitude.getValue() == 0) {
			return Result.error( "longitude cannot be 0");
		}
		if (-180 <= longitude.getValue() && 180 >= longitude.getValue()) {
			return Result.success();
		}
		return Result.error( "Expected a value between -180 and 180 but was " + longitude.getValue());
	}

	@Override
	public String toString() {
		return "CodeRule [description=" + description + ", required=" + required + " ]";
	}

	@Override
	public Message evaluate(Network network) {
		throw new IllegalArgumentException("Not supported for network.");
	}

	@Override
	public Message evaluate(Station station) {
		return run(station.getLongitude());
	}

	@Override
	public Message evaluate(Channel channel) {
		return run(channel.getLongitude());
	}

}
