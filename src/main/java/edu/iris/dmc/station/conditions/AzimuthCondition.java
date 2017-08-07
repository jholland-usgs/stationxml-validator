package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Azimuth;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Result;

public class AzimuthCondition extends AbstractCondition {

	private double min;
	private double max;

	public AzimuthCondition(boolean required, String description, double min, double max) {
		super(required, description);
	}

	@Override
	public Result evaluate(Network network) {
		throw new IllegalArgumentException("Not supported!");
	}

	@Override
	public Result evaluate(Station station) {
		throw new IllegalArgumentException("Not supported!");
	}

	@Override
	public Result evaluate(Channel channel) {
		Azimuth azimuth = channel.getAzimuth();
		if (azimuth == null) {
			if (required) {
				return Result.of(false, "Expected a value between " + min + " and " + max + " but received null.");
			}
			return Result.of(true, "");
		}
		if (azimuth.getValue() < min || azimuth.getValue() >= max) {
			return Result.of(false,
					"Expected a value between " + min + " and " + max + " but received " + azimuth.getValue());
		}
		return Result.of(true, "");
	}
}
