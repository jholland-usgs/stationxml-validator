package edu.iris.dmc.station.restrictions;

import java.util.Arrays;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Response;

public class ResponsePolynomialRestriction implements Restriction {

	private String name;

	public ResponsePolynomialRestriction() {
		this("ResponsePolynomialRestriction");
	}

	public ResponsePolynomialRestriction(String name) {
		this.name = name;
	}

	@Override
	public boolean doesQualify(Response response) {
		if (response == null) {
			throw new IllegalArgumentException("Channel|code cannot be null");
		}

		if (response.getInstrumentPolynomial() != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean doesQualify(Channel channel) {
		if(channel==null){
			return false;
		}
		return this.doesQualify(channel.getResponse());
	}

	@Override
	public String toString() {
		return "ChannelCodeRestriction [name=" + name + "]";
	}

}
