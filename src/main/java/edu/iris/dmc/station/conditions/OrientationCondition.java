package edu.iris.dmc.station.conditions;


import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Result;

public class OrientationCondition extends AbstractCondition {

	public OrientationCondition(boolean required, String description) {
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
		assert (channel != null);

		if (channel.getCode() == null) {
			// if this is an error, it should be caught somewhere else
			return Result.of(true, "");
		}

		if (channel.getCode().trim().length() < 3) {
			// if this is an error, it should be caught somewhere else
			return Result.of(true, "");
		}

		char[] array = channel.getCode().toCharArray();

		double azimuth = channel.getAzimuth().getValue();
		double dip = channel.getDip().getValue();

		boolean valid = true;
		StringBuilder messageBuilder = new StringBuilder();
		if ('E' == array[2]) {
			if (azimuth > 95 && azimuth < 85) {
				valid = false;
				messageBuilder.append("azimuth: ").append(azimuth).append(" ");
			}

			if (dip > 5 || dip < -5) {
				valid = false;
				messageBuilder.append("dip: ").append(dip).append(" ");
			}
		} else if ('N' == array[2]) {
			if (azimuth > 5 && azimuth < 355) {
				valid = false;
				messageBuilder.append("azimuth: ").append(azimuth).append(" ");
			}
			if (dip > 5 || dip < -5) {
				valid = false;
				messageBuilder.append("dip: ").append(dip).append(" ");
			}
		} else if ('Z' == array[2]) {
			if (azimuth > 5 && azimuth < 355) {
				valid = false;
				messageBuilder.append("azimuth: ").append(azimuth).append(" ");
			}
			if (dip > -85) {
				valid = false;
				messageBuilder.append("dip: ").append(dip).append(" ");
			}
		}

		if (valid) {
			return Result.of(true, "");
		}
		return Result.of(false,
				"315,Invalid channel orientation: " + messageBuilder.toString() + " for " + channel.getCode());

	}

}
