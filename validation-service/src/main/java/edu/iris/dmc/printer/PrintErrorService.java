package edu.iris.dmc.printer;

public interface PrintErrorService {
	public void header();

	public void print(edu.iris.dmc.validation.Error error);
	
	public void setPrintHandler(PrintHandler PrintHandler);
}
