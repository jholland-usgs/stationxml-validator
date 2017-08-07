package edu.iris.dmc.station.io;

import java.io.OutputStream;
import java.io.PrintStream;

import edu.iris.dmc.station.rules.Result;

public class CsvPrintStream extends PrintStream implements RuleResultPrintStream{

	public CsvPrintStream(OutputStream out) {
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

	@Override
	public void print(Result result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printRow(String text) {
		// TODO Auto-generated method stub
		
	}


}
