package edu.iris.dmc.printer;

import java.io.OutputStream;
import java.io.PrintStream;

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
	public void println(String line) {
		this.out.println(line);

	}

	@Override
	public void printHeader() {
		this.out.println(this.header);

	}

}
