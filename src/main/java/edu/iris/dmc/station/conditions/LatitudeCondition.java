package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Latitude;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class LatitudeCondition extends AbstractCondition {
	public LatitudeCondition(boolean required, String description) {
		super(required, description);
	}

	private Message run(Latitude latitude) {
		if (latitude == null) {
			return Result.error( "Latitude cannot be null");
		}

		if (-90 <= latitude.getValue() && 90 >= latitude.getValue()) {
			return Result.success();
		}
		return Result.error( "Expected a value between -90 and 90 but was " + latitude.getValue());
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
		return run(station.getLatitude());
	}

	@Override
	public Message evaluate(Channel channel) {
		return run(channel.getLatitude());
	}

}
