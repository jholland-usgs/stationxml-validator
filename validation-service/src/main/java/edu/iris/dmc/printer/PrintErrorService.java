package edu.iris.dmc.printer;

public interface PrintErrorService {
	public void header();

	public void print(edu.iris.dmc.service.Error error);
	public void print(edu.iris.dmc.service.Error error, String file);
	
	public void setPrintHandler(PrintHandler PrintHandler);
}
