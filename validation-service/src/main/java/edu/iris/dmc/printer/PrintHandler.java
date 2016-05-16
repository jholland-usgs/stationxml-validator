package edu.iris.dmc.printer;

import java.io.OutputStream;

public interface PrintHandler {

	public void setHeader(String header);

	public void setStream(OutputStream out);

	public String getDelimiter();

	public void println(String line);

	public void printHeader();
}
