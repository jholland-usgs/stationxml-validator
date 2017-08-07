package edu.iris.dmc.station.conditions;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.XmlUtil;
import edu.iris.dmc.station.rules.Result;

public class EpochRangeCondition extends AbstractCondition {

	public EpochRangeCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Result evaluate(Network network) {
		if (network == null || network.getStartDate() == null) {
			return Result.of(true, null);
		}

		return compare(network.getStartDate(), network.getEndDate());
	}

	@Override
	public Result evaluate(Station station) {
		if (station == null || station.getStartDate() == null) {
			return Result.of(true, null);
		}

		return compare(station.getStartDate(), station.getEndDate());
	}

	@Override
	public Result evaluate(Channel channel) {
		if (channel == null || channel.getStartDate() == null) {
			return Result.of(true, null);
		}

		return compare(channel.getStartDate(), channel.getEndDate());
	}

	private Result compare(XMLGregorianCalendar xmlstart, XMLGregorianCalendar xmlend) {
		if (xmlend == null) {
			return Result.of(true, null);
		}
		Date start = XmlUtil.toDate(xmlstart);
		Date end = XmlUtil.toDate(xmlend);
		boolean bool = start.getTime() <= end.getTime();
		if (bool) {
			return Result.of(true, null);
		}
		return Result.of(false,
				"endDate " + XmlUtil.toText(end) + " cannot be before startDate " + XmlUtil.toText(start));
	}

}
