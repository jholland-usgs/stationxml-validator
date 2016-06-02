package edu.iris.dmc.print;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CSVPrintHandler extends AbstractBasicPrintHandler {

	private String delimiter;
	private boolean summary;

	private static String[] FULL_HEADER = new String[] { "file", "rule-id", "rule-message", "network",
			"network-start-time", "network-end-time", "station", "station-start-time", "station-end-time", "location",
			"channel-code", "channel-start-time", "channel-end-time" };
	private static String[] SUMMARY_HEADER = new String[] { "file", "rule-id", "network", "station", "location",
			"channel-code", "channel-start-time", "channel-end-time" };

	public CSVPrintHandler(PrintStream stream) {
		this(stream, ",");
	}

	public CSVPrintHandler(PrintStream stream, String delimiter) {
		this(stream, delimiter, false);
	}

	public CSVPrintHandler(PrintStream stream, String delimiter, boolean summary) {
		super(stream);
		this.delimiter = delimiter;
		this.summary = summary;
	}

	@Override
	public void print(edu.iris.dmc.service.Error error, String file) {
		printMessage(file, error.getId(), error.getNetwork(), error.getnStart(), error.getnEnd(), error.getStation(),
				error.getsStart(), error.getsEnd(), error.getLocation(), error.getChannel(), error.getcStart(),
				error.getcEnd(), error.getMessage());
	}

	@Override
	public void printHeader() {
		int i = 0;
		for (String s : getHeader()) {
			if (i > 0) {
				stream.print(this.delimiter);
			}
			stream.print(s);
			i++;
		}

		stream.println();
	}

	public String[] getHeader() {
		if (this.summary) {
			return SUMMARY_HEADER;
		} else {
			return FULL_HEADER;
		}
	}

	private String buildMessage(Object... args) {
		StringBuilder builder = new StringBuilder();
		for (Object obj : args) {
			builder.append(obj).append(this.delimiter);
		}
		return builder.substring(0, builder.length() - 1);
	}

	private void printMessage(String file, int id, String network, Date nStart, Date nEnd, String station, Date sStart,
			Date sEnd, String location, String channel, Date cStart, Date cEnd, String message) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String line = "";
		if (this.summary) {
			line = buildMessage(file, id, network, (station != null) ? station : "", (location != null) ? location : "",
					(channel != null) ? channel : "", (cStart != null) ? sdf.format(cStart) : "",
					(cEnd != null) ? sdf.format(cEnd) : "");
		} else {
			line = buildMessage(file, id, message, network, (nStart != null) ? sdf.format(nStart) : "",
					(nEnd != null) ? sdf.format(nEnd) : "", (station != null) ? station : "",
					(sStart != null) ? sdf.format(sStart) : "", (sEnd != null) ? sdf.format(sEnd) : "",
					(location != null) ? location : "", (channel != null) ? channel : "",
					(cStart != null) ? sdf.format(cStart) : "", (cEnd != null) ? sdf.format(cEnd) : "", message);
		}
		this.stream.println(line);
	}
}
