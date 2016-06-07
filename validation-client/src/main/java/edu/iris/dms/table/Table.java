package edu.iris.dms.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.iris.dms.table.view.ALIGN;

public class Table {

	protected TableModel dataModel;
	private int border;
	private int cellspacing;
	private int cellpadding;
	private int rowSize;
	private int columnSize;
	private String header;
	private int width;
	private List<Column> columns = new ArrayList<Column>();;

	private List<Cell> cells = new ArrayList<Cell>();
	private List<Row> rows = new ArrayList<Row>();

	public Table() {
		this(null);
	}

	public Table(String header) {
		this.header = header;
	}

	public Table(String header, Column... columns) {
		this.columns = Arrays.asList(columns);
		this.header = header;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public List<Column> getColumns() {
		return this.columns;
	}

	public Column getColumn(int index) {
		if (this.columns.isEmpty()) {
			return null;
		}
		return this.columns.get(index);
	}

	public void addAll(Column... columns) {
		for (Column c : columns) {
			this.columns.add(c);
		}
	}

	public void setColumns(Column... columns) {
		this.columns = Arrays.asList(columns);
	}

	public void add(int row, int column, String value) {
		this.add(row, column, value, ALIGN.LEFT);
	}

	public void add(int row, int column, String value, ALIGN alignment) {
		this.add(row, column, value, alignment, 0);
	}

	public void add(int row, int column, String value, ALIGN alignment, int cellPadding) {
		this.add(row, column, value, alignment, cellPadding, 0, 0);
	}

	public void add(int rowIndex, int columnIndex, String value, ALIGN alignment, int cellPadding, int rowSpan,
			int colSpan) {

		if (rowSize < rowIndex) {
			rowSize = rowIndex;
		}
		if (columnSize < columnIndex) {
			columnSize = columnIndex;
		}

		Cell cell = new Cell(new Index(rowIndex, columnIndex), value, alignment, cellPadding, rowSpan, colSpan);

		if (this.rows.isEmpty() || rowIndex >= this.rows.size()) {
			Row r = new Row(rowIndex);
			this.add(r);
		}
		Row r = this.rows.get(rowIndex);
		r.add(cell);

		cells.add(cell);
	}

	private void add(Row row) {
		row.setTable(this);
		this.rows.add(row.getIndex(), row);
	}

	public List<Cell[]> getCells() {
		return create();
	}
	/*
	 * public void setHeader(int index, String title, int width, ALIGN
	 * alignment, int padding) { if (index >= this.columns.size()) { int i = 0;
	 * for (i = this.columns.size(); i < index; i++) { Column column = new
	 * Column(i); this.getColumns().add(column); } Column column = new Column(i,
	 * width); column.setHeader(title, alignment);
	 * column.setHeaderPadding(padding); this.getColumns().add(column); } else {
	 * Column column = this.columns.get(index); column.setHeader(title,
	 * alignment); column.setWidth(width); column.setHeaderPadding(padding); } }
	 */

	private List<Cell[]> create() {
		List<Cell[]> list = new ArrayList<Cell[]>();// Cell[this.rowSize][this.columnSize];

		for (int i = 0; i < this.rowSize; i++) {
			list.add(new Cell[this.columnSize]);
		}
		for (Cell c : this.cells) {
			list.get(c.getRow().getIndex())[c.getIndex().getColumn()] = c;
		}
		return list;
	}

	public int getBorder() {
		return border;
	}

	public void setBorder(int border) {
		this.border = border;
	}

	public int getCellspacing() {
		return cellspacing;
	}

	public void setCellspacing(int cellspacing) {
		this.cellspacing = cellspacing;
	}

	public int getCellpadding() {
		return cellpadding;
	}

	public void setCellpadding(int cellpadding) {
		this.cellpadding = cellpadding;
	}

	public List<Row> getRows() {
		return rows;
	}

	public int[] getSize() {
		return new int[] { this.rowSize, this.columnSize };
	}

	public int getWidth() {
		int width = 0;
		for (Column c : this.columns) {
			width += c.getWidth();
		}
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
