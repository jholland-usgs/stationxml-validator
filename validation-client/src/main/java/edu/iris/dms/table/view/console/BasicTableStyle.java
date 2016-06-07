package edu.iris.dms.table.view.console;

import edu.iris.dms.table.view.Style;

public class BasicTableStyle implements Style {

	private String columnSeperator = ":";
	private String rowSeperator = "-";
	private int spacing = 1;

	@Override
	public String getColumnSeperator() {
		return columnSeperator;
	}

	@Override
	public String getRowSeperator() {
		return rowSeperator;
	}
	public int getSpacing() {
		return spacing;
	}

	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}
}
