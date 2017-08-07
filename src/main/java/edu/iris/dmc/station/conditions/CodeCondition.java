package edu.iris.dmc.station.conditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.iris.dmc.fdsn.station.model.BaseNodeType;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Result;

public class CodeCondition extends AbstractCondition {
	private String regex;

	public CodeCondition(boolean required, String regex, String description) {
		super(required, description);
		this.regex = regex;
	}

	public String getRegex() {
		return this.regex;
	}

	private Result run(BaseNodeType t) {
		String code = t.getCode();
		if (code == null) {
			if (!required) {
				return Result.of(true, "");
			}
			return Result.of(false, "Expected a value like" + this.regex + " but was null.");
		}

		Pattern p = Pattern.compile(this.regex);
		Matcher m = p.matcher(code);

		if (!m.matches()) {
			return Result.of(false, "Expected a value like" + this.regex + " but was " + t.getCode());
		}
		return Result.of(true, "");
	}

	@Override
	public String toString() {
		return "CodeRule [description=" + description + ", required=" + required + ", regex=" + regex + " ]";
	}

	@Override
	public Result evaluate(Network network) {
		return run(network);
	}

	@Override
	public Result evaluate(Station station) {
		return run(station);
	}

	@Override
	public Result evaluate(Channel channel) {
		return run(channel);
	}

}
