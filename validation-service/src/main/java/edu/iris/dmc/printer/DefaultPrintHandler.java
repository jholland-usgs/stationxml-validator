package edu.iris.dmc.printer;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import edu.iris.dmc.service.Error;

public class DefaultPrintHandler implements PrintHandler {
	private PrintStream out;
	private String delimiter;
	private String header;

	public DefaultPrintHandler() {
		this(System.out,
				"rule-id,rule-message,network,network-start-time,network-end-time,station,station-start-time,station-end-time,location,channel-code,channel-start-time,channel-end-time",
				",");
	}

	public DefaultPrintHandler(PrintStream out, String header, String delimiter) {
		this.out = out;
		this.delimiter = delimiter;
		this.header = header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public void setStream(OutputStream out) {
		this.out = new PrintStream(out);
	}

	public PrintStream getOut() {
		return out;
	}

	public void setOut(PrintStream out) {
		this.out = out;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getHeader() {
		return header;
	}

	@Override
	public void printHeader() {
		this.out.println(this.header);

	}

	@Override
	public void print(Error error, String file) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		this.out.println(file + "," + error.getId() + "," + error.getNetwork() + ","
				+ ((error.getnStart() != null) ? sdf.format(error.getnStart()) : "") + ","
				+ ((error.getnEnd() != null) ? sdf.format(error.getnEnd()) : "") + ","
				+ ((error.getStation() != null) ? error.getStation() : "") + ","
				+ ((error.getsStart() != null) ? sdf.format(error.getsStart()) : "") + ","
				+ ((error.getsEnd() != null) ? sdf.format(error.getsEnd()) : "") + ","
				+ ((error.getLocation() != null) ? error.getLocation() : "") + ","
				+ ((error.getChannel() != null) ? error.getChannel() : "") + ","
				+ ((error.getcStart() != null) ? sdf.format(error.getcStart()) : "") + ","
				+ ((error.getcEnd() != null) ? sdf.format(error.getcEnd()) : "") + "," + error.getMessage());

	}

	@Override
	public void print(String message) {
		this.out.println(message);
	}

	@Override
	public void flush() {
		this.out.flush();
	}

}
