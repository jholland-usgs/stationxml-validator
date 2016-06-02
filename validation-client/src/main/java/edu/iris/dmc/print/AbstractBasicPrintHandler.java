package edu.iris.dmc.print;

import java.io.PrintStream;

import edu.iris.dmc.printer.PrintHandler;

public abstract class AbstractBasicPrintHandler implements PrintHandler {

	protected PrintStream stream;

	public AbstractBasicPrintHandler(PrintStream stream) {
		this.stream = stream;
	}

	@Override
	public void setHeader(String header) {
		// TODO Auto-generated method stub

	}

}
