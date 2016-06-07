package edu.iris.dms.table;

import java.util.ArrayList;
import java.util.List;

import edu.iris.dms.table.view.ALIGN;

public class Row {

	private Table table;
	private int index;
	private int group;
	private List<Cell> cells = new ArrayList<Cell>();

	public Row(int index) {
		this.index = index;
	}

	public void add(Cell cell) {
		cell.setRow(this);
		if (cell.getIndex().getColumn() >= this.cells.size()) {
			for (int i = this.cells.size(); i < cell.getIndex().getColumn(); i++) {
				Cell c = new Cell(new Index(this.index, i), " ", ALIGN.LEFT);
				this.cells.add(c);
			}
		}
		this.cells.add(cell.getIndex().getColumn(), cell);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<Cell> getCells() {
		return cells;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

}
