package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class OrientationCondition extends AbstractCondition {

	public OrientationCondition(boolean required, String description) {
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
		assert (channel != null);

		if (channel.getCode() == null ||channel.getCode().trim().isEmpty()|| channel.getAzimuth() == null || channel.getDip() == null) {
			return Result.success();
		}
		char[] array = channel.getCode().toCharArray();

		double azimuth = channel.getAzimuth().getValue();
		double dip = channel.getDip().getValue();

		boolean valid = true;
		StringBuilder messageBuilder = new StringBuilder();
		if ('E' == array[2]) {
			if (azimuth < 95 && azimuth > 85 || azimuth < 275 && azimuth > 265) {
				
			}else{
				valid = false;
				messageBuilder.append("azimuth: ").append(azimuth).append(" ");
			}

			if (dip > 5 || dip < -5) {
				valid = false;
				messageBuilder.append("dip: ").append(dip).append(" ");
			}
		} else if ('N' == array[2]) {
			if (azimuth <= 5 && azimuth >= 0 || azimuth >=355 && azimuth <= 360 || (azimuth <= 185 && azimuth >= 175)) {

			} else {
				valid = false;
				messageBuilder.append("azimuth: ").append(azimuth).append(" ");
			}
			if (dip < 5 || dip > -5) {

			} else {
				valid = false;
				messageBuilder.append("dip: ").append(dip).append(" ");
			}
		} else if ('Z' == array[2]) {
			if (azimuth > 5 && azimuth < 355) {
				valid = false;
				messageBuilder.append("azimuth: ").append(azimuth).append(" ");
			}
			if (dip > -95 && dip < -85 || (dip > 85 && dip < 95)) {

			} else {
				valid = false;
				messageBuilder.append("dip: ").append(dip).append(" ");
			}
		}

		if (valid) {
			return Result.success();
		}
		return Result.error("Invalid channel orientation: " + messageBuilder.toString() + " for " + channel.getCode());

	}

}
