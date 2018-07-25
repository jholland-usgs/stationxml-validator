package edu.iris.dmc.station.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class XmlPrintStream extends PrintStream implements RuleResultPrintStream {

	public XmlPrintStream(OutputStream out) {
		super(out);
	}

	public void println(Result result) {

	}

	@Override
	public void printHeader() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printFooter() {
		// TODO Auto-generated method stub

	}

	public void print(Message result) {
		print("", result);
	}

	@Override
	public void print(String source, Message result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void printRow(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void printMessage(String text) throws IOException {
		// TODO Auto-generated method stub

	}

}
