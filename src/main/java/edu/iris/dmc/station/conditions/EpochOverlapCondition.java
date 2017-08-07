package edu.iris.dmc.station.conditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.iris.dmc.fdsn.station.model.BaseNodeType;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.XmlUtil;
import edu.iris.dmc.station.rules.Result;

public class EpochOverlapCondition extends AbstractCondition {

	public EpochOverlapCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Result evaluate(FDSNStationXML document) {
		if (document == null) {
			return Result.of(true, null);
		}

		if (document.getNetwork() == null || document.getNetwork().isEmpty()) {
			return Result.of(true, null);
		}
		
		Result result = run(document.getNetwork());
		return result;
	}

	@Override
	public Result evaluate(Network network) {
		if (network == null) {
			return Result.of(true, null);
		}

		if (network.getStations() == null || network.getStations().isEmpty()) {
			return Result.of(true, null);
		}
		Result result = run(network.getStations());
		result.setNetwork(network);
		return result;
	}

	@Override
	public Result evaluate(Station station) {
		if (station == null) {
			return Result.of(true, null);
		}

		if (station.getChannels() == null || station.getChannels().isEmpty()) {
			return Result.of(true, null);
		}
		Result result = run(station.getChannels());
		result.setStation(station);
		return result;
	}

	@Override
	public Result evaluate(Channel channel) {
		throw new IllegalArgumentException("Not supported for channels");
	}

	private Result run(List<? extends BaseNodeType> list) {

		int i = 0;
		Map<String, List<Tuple>> map = new HashMap<String, List<Tuple>>();
		for (BaseNodeType node : list) {
			node.getStartDate();
			node.getEndDate();

			Date start = XmlUtil.toDate(node.getStartDate());
			Date end = null;

			String key = node.getCode();
			if (node instanceof Channel) {
				key = key + ((Channel) node).getLocationCode();
			}
			if (node.getEndDate() != null) {
				end = XmlUtil.toDate(node.getEndDate());
			}
			List<Tuple> tuples = map.get(key);
			if (tuples == null) {
				tuples = new ArrayList<Tuple>();
				map.put(key, tuples);
			}

			if (node instanceof Channel) {
				tuples.add(new Tuple(node.getCode(), ((Channel) node).getLocationCode(), start, end, i));
			} else {
				tuples.add(new Tuple(node.getCode(), node.getCode(), start, end, i));
			}

			i++;
		}

		if (!map.isEmpty()) {
			Iterator<Entry<String, List<Tuple>>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, List<Tuple>> pair = (Map.Entry<String, List<Tuple>>) it.next();
				List<Tuple> tuples = pair.getValue();
				List<Tuple[]> invalidRanges = checkRanges(tuples);
				if (invalidRanges != null && !invalidRanges.isEmpty()) {
					StringBuilder builder = new StringBuilder();
					for (Tuple[] tuple : invalidRanges) {
						builder.append("[(").append(tuple[0].code).append(",").append(XmlUtil.toText(tuple[0].start))
								.append(",").append(XmlUtil.toText(tuple[0].end)).append(")(").append(tuple[1].code)
								.append(",").append(XmlUtil.toText(tuple[1].start)).append(",")
								.append(XmlUtil.toText(tuple[1].end)).append(")]");
					}
					Result result = Result.of(false, builder.toString());
					return result;
				}
			}
		}
		return Result.of(true, null);
	}

	private List<Tuple[]> checkRanges(List<Tuple> tuples) {
		List<Tuple[]> overlappingDatePairs = new ArrayList<Tuple[]>();
		Collections.sort(tuples);
		for (int i = 1; i < tuples.size(); i++) {
			Tuple tuple1 = tuples.get(i - 1);
			Tuple tuple2 = tuples.get(i);
			if (tuple1.end == null || tuple2.start == null || tuple1.end.getTime() > tuple2.start.getTime()) {
				overlappingDatePairs.add(new Tuple[] { tuple1, tuple2 });
			}
		}
		return overlappingDatePairs;
	}

	private class Tuple implements Comparable<Tuple> {
		public final Date start;
		private Date end;
		public final int index;
		public String code;
		public String location;

		public Tuple(String code, String location, Date start, Date end, int index) {
			this.code = code;
			this.location = location;
			this.start = start;
			this.end = end;
			this.index = index;
		}

		public int compareTo(Tuple other) {
			return start.compareTo(other.start);
		}

	}

}
