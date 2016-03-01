package edu.iris.dmc;

import java.io.PrintStream;
import java.util.Date;

import org.xml.sax.Locator;

public class PrintErrorService {

	private PrintStream out;
	private String delimiter;

	PrintErrorService(PrintStream out, String delimiter) {
		this.out = out;
		this.delimiter = delimiter;

	}

	public void header() {
		out.println(
				"rule-id,rule-message,network,network-start-time,network-end-time,station,station-start-time,station-end-time,location,channel-code,channel-start-time,channel-end-time");
	}

	public void print(edu.iris.dmc.validation.Error error) {
		printMessage(error.getNetwork(), error.getnStart(), error.getnEnd(), error.getStation(), error.getsStart(),
				error.getsEnd(), error.getLocation(), error.getChannel(), error.getcStart(), error.getcEnd(),
				error.getMessage());
	}

	private String buildMessage(Object... args) {
		StringBuilder builder = new StringBuilder();
		for (Object obj : args) {
			builder.append(obj).append(delimiter);
		}
		return builder.substring(0, builder.length() - 1);
	}

	private void printMessage(String network, Date nStart, Date nEnd, String station, Date sStart, Date sEnd,
			String location, String channel, Date cStart, Date cEnd, String message) {
		this.out.println(buildMessage(message, network, nStart, nEnd, station, (station != null) ? station : "",
				(sStart != null) ? sStart : "", (sEnd != null) ? sEnd : "", (location != null) ? location : "",
				(channel != null) ? channel : "", (cStart != null) ? cStart : "", (cEnd != null) ? cEnd : ""));
	}
}
