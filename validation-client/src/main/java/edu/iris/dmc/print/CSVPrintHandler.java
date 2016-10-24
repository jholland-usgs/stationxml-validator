package edu.iris.dmc.print;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import edu.iris.dmc.validation.rule.Severity;

public class CSVPrintHandler extends AbstractBasicPrintHandler {

	private String delimiter;
	private boolean summary;
	private boolean ignoreWarnings;

	private static String[] FULL_HEADER = new String[] { "File", "Rule-Id", "Rule-Message", "Network",
			"Network-Start-Time", "Network-End-Time", "Station", "Station-Start-Time", "Station-End-Time", "Location",
			"Channel-Code", "Channel-Start-Time", "Channel-End-Time" };
	private static String[] SUMMARY_HEADER = new String[] { "File", "Rule-Id", "Network", "Station", "Location",
			"Channel-Code", "Channel-Start-Time", "Channel-End-Time" };

	public CSVPrintHandler(PrintStream stream) {
		this(stream, ",", false);
	}

	public CSVPrintHandler(PrintStream stream, String delimiter, boolean summary) {
		this(stream, delimiter, summary, false);
	}

	public CSVPrintHandler(PrintStream stream, String delimiter, boolean summary, boolean ignoreWarnings) {
		super(stream);
		this.delimiter = delimiter;
		this.summary = summary;
		this.ignoreWarnings = ignoreWarnings;
	}

	@Override
	public void print(edu.iris.dmc.service.Error error, String file) {
		if (this.ignoreWarnings && error.getSeverity().equals(Severity.WARN)) {
			return;
		}
		printMessage(error.getSeverity(), file, error.getId(), error.getNetwork(), error.getnStart(), error.getnEnd(),
				error.getStation(), error.getsStart(), error.getsEnd(), error.getLocation(), error.getChannel(),
				error.getcStart(), error.getcEnd(), error.getMessage(), error.getInvalidValue());
	}

	@Override
	public void print(String message) {
		stream.println(message);
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

	private void printMessage(Severity severity, String file, int id, String network, Date nStart, Date nEnd,
			String station, Date sStart, Date sEnd, String location, String channel, Date cStart, Date cEnd,
			String message, Object invalidValue) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String line = "";
		if (this.summary) {
			line = buildMessage(severity, file, id, network, (station != null) ? station : "",
					(location != null) ? location : "", (channel != null) ? channel : "",
					(cStart != null) ? sdf.format(cStart) : "", (cEnd != null) ? sdf.format(cEnd) : "");
		} else {
			if (invalidValue instanceof List) {
				line = buildMessage(severity, file, id, message, network, (nStart != null) ? sdf.format(nStart) : "",
						(nEnd != null) ? sdf.format(nEnd) : "", (station != null) ? station : "",
						(sStart != null) ? sdf.format(sStart) : "", (sEnd != null) ? sdf.format(sEnd) : "",
						(location != null) ? location : "", (channel != null) ? channel : "",
						(cStart != null) ? sdf.format(cStart) : "", (cEnd != null) ? sdf.format(cEnd) : "");
			} else {
				line = buildMessage(severity, file, id, "[" + invalidValue + "] " + message, network,
						(nStart != null) ? sdf.format(nStart) : "", (nEnd != null) ? sdf.format(nEnd) : "",
						(station != null) ? station : "", (sStart != null) ? sdf.format(sStart) : "",
						(sEnd != null) ? sdf.format(sEnd) : "", (location != null) ? location : "",
						(channel != null) ? channel : "", (cStart != null) ? sdf.format(cStart) : "",
						(cEnd != null) ? sdf.format(cEnd) : "");

			}
		}
		this.stream.println(line);
	}

	@Override
	public void flush() {
		this.stream.flush();
	}
}
