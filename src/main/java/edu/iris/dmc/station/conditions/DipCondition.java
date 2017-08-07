package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Dip;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Result;

public class DipCondition extends AbstractCondition {

	private int min;
	private int max;

	public DipCondition(boolean required, String description, int min, int max) {
		super(required, description);
		this.min = min;
		this.max = max;
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

		Dip dip = channel.getDip();
		if (dip == null) {
			return Result.of(false, "expected dip value but was null");
		}

		if (dip.getValue() >= -90 && dip.getValue() <= 90) {
			return Result.of(true, null);
		}
		return Result.of(false, "expected a value between "+min+" and "+max+" but was "+dip.getValue());
	}

}
