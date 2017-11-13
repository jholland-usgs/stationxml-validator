package edu.iris.dmc.station.io;

import java.io.IOException;

import edu.iris.dmc.station.rules.Result;

public interface RuleResultPrintStream extends AutoCloseable {
	public void println(String line);

	public void printHeader() throws IOException;

	public void printFooter() throws IOException;

	public void print(Result result) throws IOException;
	
	public void print(String source, Result result) throws IOException;

	public void printRow(String text) throws IOException;

	public void printMessage(String text) throws IOException;

	public void flush() throws IOException;
}
