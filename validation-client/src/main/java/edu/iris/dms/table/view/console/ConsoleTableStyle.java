package edu.iris.dms.table.view.console;

import edu.iris.dms.table.view.Style;

public class ConsoleTableStyle implements Style {

	private String columnSeperator = ":";
	private String rowSeperator = "-";
	private int spacing = 1;
	
	public static String TOP_LEFT_JOINT = "┌";
	public static String TOP_RIGHT_JOINT = "┐";
	public static String BOTTOM_LEFT_JOINT = "└";
	public static String BOTTOM_RIGHT_JOINT = "┘";
	public static String TOP_JOINT = "┬";
	public static String BOTTOM_JOINT = "┴";
	public static String LEFT_JOINT = "├";
	public static String JOINT = "┼";
	public static String RIGHT_JOINT = "┤";
	public static char HORIZONTAL_LINE = '─';
	public static char PADDING = ' ';
	public static String VERTICAL_LINE = "│";

	public static String CELL_EDGE="|";

	public String getColumnSeperator() {
		return columnSeperator;
	}

	public void setColumnSeperator(String columnSeperator) {
		this.columnSeperator = columnSeperator;
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
