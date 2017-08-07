package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Distance;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Result;

public class StationElevationCondition extends AbstractCondition {
	public StationElevationCondition(boolean required, String description) {
		super(required, description);
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
		if (station.getChannels() == null || station.getChannels().isEmpty()) {
			return Result.of(true, null);
		}
		Distance distance = station.getElevation();
		if (distance == null) {

		}
		if (station.getElevation() == null) {
			return Result.of(true, null);
		}
		for (Channel channel : station.getChannels()) {
			if (channel.getElevation() != null) {
				if (channel.getElevation().getValue() > station.getElevation().getValue()) {
					return Result.of(false, "expected "+station.getCode()+" elevation "+station.getElevation().getValue()+" to be equal to or above "+channel.getCode()+":"+channel.getLocationCode()+
							" elevation "+channel.getElevation().getValue() );
				}
			}
		}
		return Result.of(true, null);
	}

	@Override
	public Result evaluate(Channel channel) {
		throw new IllegalArgumentException("Not supported for channel.");
	}

}
