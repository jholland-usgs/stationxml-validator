package edu.iris.dms.table;

public class Column {

	private String header;
	private int width;

	public Column(String header) {
		this(header, 0);
	}

	public Column(String header, int width) {
		this.header = header;
		this.width = width;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	
}
