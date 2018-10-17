package edu.iris.dmc.station.restrictions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Response;

public interface Restriction {

	public boolean qualifies(Channel channel);
	public boolean qualifies(Response response);
}
