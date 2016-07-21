package edu.iris.dmc.printer;

public class PrintErrorServiceImp implements PrintErrorService {

	private PrintHandler printHandler;

	public PrintErrorServiceImp() {
		printHandler = new DefaultPrintHandler();
	}

	public void header() {
		printHandler.printHeader();
	}

	public void print(edu.iris.dmc.service.Error error) {
		this.print(error, "");
	}

	public void print(edu.iris.dmc.service.Error error, String file) {
		this.printHandler.print(error, file);
	}

	public void print(String message) {
		this.printHandler.print(message);
	}

	@Override
	public void setPrintHandler(PrintHandler PrintHandler) {
		this.printHandler = PrintHandler;
	}

	public PrintHandler getPrintHandler() {
		return printHandler;
	}

	public void flush() {
		this.printHandler.flush();
	}
}
