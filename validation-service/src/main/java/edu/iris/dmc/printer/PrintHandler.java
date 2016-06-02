package edu.iris.dmc.printer;

public interface PrintHandler {

	public void setHeader(String header);

	public void print(edu.iris.dmc.service.Error error, String file);

	public void printHeader();
}
