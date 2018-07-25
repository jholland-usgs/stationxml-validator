package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class DistanceCondition extends AbstractCondition {

	private int margin;

	public DistanceCondition(boolean required, String description, int margin) {
		super(required, description);
		this.margin = margin;
	}

	@Override
	public Message evaluate(Network network) {
		throw new IllegalArgumentException("method not supported for network.");
	}

	@Override
	public Message evaluate(Station station) {

		if (station.getChannels() == null || station.getChannels().isEmpty()) {
			return Result.success();
		}

		for (Channel channel : station.getChannels()) {
			if (channel.getLatitude() == null || channel.getLongitude() == null) {

			}
			double distance = DistanceCalculator.distance(channel.getLatitude().getValue(),
					channel.getLongitude().getValue(), station.getLatitude().getValue(),
					station.getLongitude().getValue(), "K");
			if (distance > this.margin) {
				return Result.error(
						"Expected a distance difference of less than " + margin + " between "+station.getCode()+" and "+channel.getCode()+":"+channel.getLocationCode()+", but was " + distance);
			}
		}

		return Result.success();

	}

	@Override
	public Message evaluate(Channel channel) {
		throw new IllegalArgumentException("method not supported for channel.");
	}

}
