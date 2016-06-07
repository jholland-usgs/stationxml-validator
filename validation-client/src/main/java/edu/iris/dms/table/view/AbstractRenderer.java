package edu.iris.dms.table.view;

import java.io.PrintStream;

import edu.iris.dms.table.Table;
import edu.iris.dms.table.view.console.BasicTableStyle;

public abstract class AbstractRenderer<T> implements Renderer<T> {

	private PrintStream stream;
	protected Style style = new BasicTableStyle();

	public AbstractRenderer(PrintStream stream) {
		this.stream = stream;
	}

	public AbstractRenderer(PrintStream stream, Style style) {
		this.stream = stream;
		this.style = style;
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

	public PrintStream getStream() {
		return stream;
	}

}
