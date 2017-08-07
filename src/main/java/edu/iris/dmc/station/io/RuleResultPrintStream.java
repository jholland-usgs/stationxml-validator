package edu.iris.dmc.station.io;

import edu.iris.dmc.station.rules.Result;

public interface RuleResultPrintStream extends AutoCloseable{
	public void println(String line);

	public void printHeader();
	public void printFooter();
	public void print(Result result);
	public void printRow(String text);
	public void flush();
}
