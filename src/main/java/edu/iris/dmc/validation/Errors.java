package edu.iris.dmc.validation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xml.sax.Locator;

public class Errors {

	private List<Error> list = new ArrayList<Error>();

	public void add(Error error) {
		this.list.add(error);
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public void add(String network, Date nStart, Date nEnd,
			String station, Date sStart, Date sEnd, String location, String channel,
			Date cStart, Date cEnd, String message) {
		
		this.list.add(new Error(network,nStart,nEnd,station,sStart,sEnd,location,channel,cStart,cEnd,message));
	}

	public List<Error> getAll() {
		return this.list;
	}
}
