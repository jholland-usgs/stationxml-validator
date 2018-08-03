package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Message;

public interface Condition {
	public boolean isRequired();

	public String getDescription();

	public Message evaluate(FDSNStationXML document);
	
	public Message evaluate(Network network);

	public Message evaluate(Station station);

	public Message evaluate(Channel channel);
	
	public Message evaluate(Channel channel,Response response);
}
