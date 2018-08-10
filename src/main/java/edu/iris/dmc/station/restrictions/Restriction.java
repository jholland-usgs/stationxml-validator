package edu.iris.dmc.station.restrictions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Response;

public interface Restriction {

	public boolean doesQualify(Channel channel);
	public boolean doesQualify(Response response);
}
