package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.station.rules.Result;

public abstract class AbstractCondition implements Condition {

	protected boolean required;
	protected String description;

	public AbstractCondition(boolean required, String description) {
		this.required = required;
		this.description = description;
	}

	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Result evaluate(FDSNStationXML document) {
		throw new IllegalArgumentException("method not supported");
	}

	@Override
	public Result evaluate(Channel channel, Response response) {
		throw new IllegalArgumentException("method not supported");
	}

}
