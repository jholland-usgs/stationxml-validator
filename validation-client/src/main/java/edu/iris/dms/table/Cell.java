package edu.iris.dms.table;

import edu.iris.dms.table.view.ALIGN;

public class Cell {

	private Index index;
	private String data;
	private ALIGN alignment = ALIGN.LEFT;

	private Row row;

	private int padding;
	private int width;

	private int colSpan = 0;
	private int rowSpan = 0;

	public Cell(Index index, String text) {
		this(index, text, ALIGN.LEFT, 0);
	}

	public Cell(Index index, String text, ALIGN alignment) {
		this(index, text, alignment, 0);
	}

	public Cell(Index index, String text, ALIGN alignment, int cellpadding) {
		this(index, text, alignment, cellpadding, 0, 0);
	}

	public Cell(Index index, String data, ALIGN alignment, int cellpadding, int rowSpan, int colSpan) {
		this.index = index;
		this.data = data;
		this.alignment = alignment;
		this.rowSpan = rowSpan;
		this.colSpan = colSpan;
		this.padding = cellpadding;
	}

	public Index getIndex() {
		return index;
	}

	public void setIndex(Index index) {
		this.index = index;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public ALIGN getAlignment() {
		return alignment;
	}

	public void setAlignment(ALIGN alignment) {
		this.alignment = alignment;
	}

	public Row getRow() {
		return this.row;
	}

	public void setRow(Row row) {
		this.row = row;
	}

	public int getPadding() {
		return padding;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
