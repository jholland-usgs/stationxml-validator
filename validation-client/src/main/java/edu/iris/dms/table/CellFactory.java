package edu.iris.dms.table;

public class CellFactory {

	public static Cell create(String data) {
		return CellFactory.create(data, 0, 0);
	}

	public static Cell create(String data, int row, int column) {
		return new Cell(new Index(row, column), data);
	}
}
