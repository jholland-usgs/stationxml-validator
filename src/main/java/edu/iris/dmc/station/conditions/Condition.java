package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Result;

public interface Condition {
	public boolean isRequired();

	public String getDescription();

	public Result evaluate(FDSNStationXML document);
	
	public Result evaluate(Network network);

	public Result evaluate(Station station);

	public Result evaluate(Channel channel);
	
	public Result evaluate(Response response);
}
