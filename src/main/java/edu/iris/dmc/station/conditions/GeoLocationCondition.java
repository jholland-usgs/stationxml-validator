package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Latitude;
import edu.iris.dmc.fdsn.station.model.Longitude;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Result;

public class GeoLocationCondition extends AbstractCondition {

	public GeoLocationCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Result evaluate(Network network) {
		throw new IllegalArgumentException("method not supported for network.");
	}

	@Override
	public Result evaluate(Station station) {
		Latitude latitude = station.getLatitude();
		Longitude longtiude = station.getLongitude();
		if (latitude == null || longtiude == null) {
			return Result.of(true, null);
		}
		if (latitude.getValue() == 0 && longtiude.getValue() == 0) {
			return Result.of(false,
					"latitude: " + latitude.getValue() + " or longitude " + longtiude.getValue() + " cannot be 0");
		}
		return Result.of(true, null);
	}

	@Override
	public Result evaluate(Channel channel) {
		Latitude latitude = channel.getLatitude();
		Longitude longtiude = channel.getLongitude();
		if (latitude == null || longtiude == null) {
			return Result.of(true, null);
		}
		if (latitude.getValue() == 0 && longtiude.getValue() == 0) {
			return Result.of(false,
					"latitude: " + latitude.getValue() + " or longitude " + longtiude.getValue() + " cannot be 0");
		}
		return Result.of(true, null);
	}

}
