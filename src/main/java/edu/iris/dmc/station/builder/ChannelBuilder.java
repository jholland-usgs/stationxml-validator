package edu.iris.dmc.station.builder;

import java.math.BigDecimal;

import edu.iris.dmc.TimeUtil;
import edu.iris.dmc.fdsn.station.model.Azimuth;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Channel.ClockDrift;
import edu.iris.dmc.fdsn.station.model.Dip;
import edu.iris.dmc.fdsn.station.model.Distance;
import edu.iris.dmc.fdsn.station.model.Latitude;
import edu.iris.dmc.fdsn.station.model.Longitude;
import edu.iris.dmc.fdsn.station.model.SampleRate;
import edu.iris.dmc.seed.BTime;
import edu.iris.dmc.seed.control.station.B052;

public class ChannelBuilder extends AbstractBuilder {

	public static Channel build(B052 blockette) throws Exception {
		String location = blockette.getLocationCode();
		String chanCode = blockette.getChannelCode();

		Channel channel = factory.createChannelType();

		channel.setCode(chanCode);
		channel.setLocationCode(location.trim());

		BTime bTime = blockette.getStartTime();
		if (bTime != null) {
			channel.setStartDate(TimeUtil.toCalendar(bTime));
		}

		bTime = blockette.getEndTime();
		if (bTime != null) {
			channel.setEndDate(TimeUtil.toCalendar(bTime));
		}

		Latitude latitude = factory.createLatitudeType();
		latitude.setValue(blockette.getLatitude());
		channel.setLatitude(latitude);

		Longitude longitude = factory.createLongitudeType();
		longitude.setValue(blockette.getLongitude());
		channel.setLongitude(longitude);

		Distance elevation = factory.createDistanceType();
		elevation.setValue(blockette.getElevation());
		channel.setElevation(elevation);

		Distance depth = factory.createDistanceType();
		depth.setValue((Double) blockette.getLocalDepth());
		channel.setDepth(depth);

		Dip dip = factory.createDipType();
		dip.setValue(blockette.getDip());
		channel.setDip(dip);

		Double azimuthValue = blockette.getAzimuth();

		if (azimuthValue != null) {
			Azimuth azimuth = factory.createAzimuthType();
			if (dip != null && (dip.getValue() == 90 || dip.getValue() == -90)) {
				if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(azimuthValue)) != 0) {
					if (azimuthValue.intValue() >= 360) {
						azimuth.setValue(0);
					} else {
						azimuth.setValue(azimuthValue);
					}
				}
			} else {
				azimuth.setValue(azimuthValue);
			}
			channel.setAzimuth(azimuth);
		}

		SampleRate sampleRate = factory.createSampleRateType();
		sampleRate.setValue(blockette.getSampleRate());
		channel.setSampleRate(sampleRate);

		ClockDrift clockDrift = factory.createChannelTypeClockDrift();
		clockDrift.setValue(blockette.getMaxClockDrift());
		channel.setClockDrift(clockDrift);

		blockette.getChannelFlags();
		String cType = blockette.getChannelFlags();
		if (cType != null) {
			for (int i = 0; i < cType.length(); i++) {
				char kar = cType.charAt(i);
				if ('C' == kar) {
					channel.addType("CONTINUOUS");
				} else if ('G' == kar) {
					channel.addType("GEOPHYSICAL");
				} else if ('T' == kar) {
					channel.addType("TRIGGERED");
				} else if ('H' == kar) {
					channel.addType("HEALTH");
				} else if ('W' == kar) {
					channel.addType("WEATHER");
				} else if ('F' == kar) {
					channel.addType("FLAG");
				} else if ('S' == kar) {
					channel.addType("SYNTHESIZED");
				} else if ('I' == kar) {
					channel.addType("INPUT");
				} else if ('E' == kar) {
					channel.addType("EXPERIMENTAL");
				} else if ('M' == kar) {
					channel.addType("MAINTENANCE");
				} else if ('B' == kar) {
					channel.addType("BEAM");
				} else {

				}
			}
		}

		return channel;

	}
}
