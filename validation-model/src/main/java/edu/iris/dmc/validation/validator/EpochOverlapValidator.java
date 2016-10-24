package edu.iris.dmc.validation.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.NodeType;
import edu.iris.dmc.validation.rule.NoOverlap;

public class EpochOverlapValidator implements ConstraintValidator<NoOverlap, Collection<?>> {
	private NoOverlap constraintAnnotation;

	@Override
	public void initialize(NoOverlap constraintAnnotation) {
		this.constraintAnnotation = constraintAnnotation;

	}

	@Override
	public boolean isValid(Collection<?> collection, ConstraintValidatorContext context) {
		if (collection == null || collection.isEmpty() || collection.size() == 1) {
			// null collection cannot be validated
			return true;
		}

		int i = 0;
		Map<String, List<Tuple>> map = new HashMap<String, List<Tuple>>();
		for (Object object : collection) {
			if (!(object instanceof NodeType)) {
				// throw
			}
			NodeType node = (NodeType) object;
			Date start = node.getStartDate();
			Date end = null;

			String key = node.getCode();
			if (node instanceof Channel) {
				key = key + ((Channel) node).getLocationCode();
			}
			if (node.getEndDate() != null) {
				end = node.getEndDate();
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
					return false;
				}
			}
		}
		return true;
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
