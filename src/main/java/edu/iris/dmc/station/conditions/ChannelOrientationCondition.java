package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class ChannelOrientationCondition extends AbstractCondition {

	public ChannelOrientationCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Message evaluate(Network network) {
		throw new IllegalArgumentException("Not supported!");
	}

	@Override
	public Message evaluate(Station station) {
		throw new IllegalArgumentException("Not supported!");
	}

	@Override
	public Message evaluate(Channel channel) {
		if (channel.getCode() == null) {
			// if this is an error, it should be caught somewhere else
			return Result.success();
		}

		if (channel.getCode().trim().length() < 3) {
			// if this is an error, it should be caught somewhere else
			return Result.success();
		}

		char[] array = channel.getCode().toCharArray();

		double azimuth = channel.getAzimuth().getValue();
		double dip = channel.getDip().getValue();

		boolean valid = true;
		StringBuilder messageBuilder = new StringBuilder();
		if ('E' == array[2]) {
			boolean azimuth_range = ((azimuth > 85 && azimuth < 95) || (azimuth > 265 && azimuth < 275));
			if (! azimuth_range) {
				valid = false;
				messageBuilder.append("azimuth: ").append(azimuth).append(" ");
			}
			if (dip > 5 || dip < -5) {
				valid = false;
				messageBuilder.append("dip: ").append(dip).append(" ");
			}
		} else if ('N' == array[2]) {
			if ((azimuth > 5 && azimuth < 355) || (azimuth > 175 && azimuth < 185)) {
				valid = false;
				messageBuilder.append("azimuth: ").append(azimuth).append(" ");
			}
			if (dip > 5 || dip < -5) {
				valid = false;
				messageBuilder.append("dip: ").append(dip).append(" ");
			}
		} else if ('Z' == array[2]) {
			if ((azimuth > 5 && azimuth < 355) || (azimuth > 175 && azimuth < 185)) {
				valid = false;
				messageBuilder.append("azimuth: ").append(azimuth).append(" ");
			}
			if (dip > -85) {
				valid = false;
				messageBuilder.append("dip: ").append(dip).append(" ");
			}
		}

		if (valid) {
			return Result.success();
		}
		return Result.error(
				"Invalid channel orientation: " + messageBuilder.toString() + " for " + channel.getCode());
	}
}
