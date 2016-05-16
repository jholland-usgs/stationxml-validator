package edu.iris.dmc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public class Table {

	private List<List<Cell>> table = new ArrayList<List<Cell>>();
	private int width;

	public Table(int width) {
		this.width = width;
	}

	public void add(int row, int column, String text) {
		add(row, column, text, ALIGN.LEFT, -1);
	}

	public void add(int row, int column, String text, ALIGN align, int width) {
		List<Cell> columns = null;
		if (table.isEmpty() || row >= table.size()) {
			columns = new ArrayList<Cell>();
			table.add(row, columns);
		} else {
			columns = table.get(row);
		}
		columns.add(column, new Cell(row, column, text, align, width));
	}

	public String print() {
		StringBuilder builder = new StringBuilder();
		int index = 0;
		for (List<Cell> row : table) {
			builder.append(printRow(row));
			index = 0;
		}
		return builder.toString();
	}

	private String printRow(List<Cell> row) {
		int index = 0;

		StringBuilder builder = new StringBuilder();
		while (true) {
			boolean BREAK = true;
			for (int i = 0; i < row.size(); i++) {
				Cell cell = row.get(i);
				int height = cell.lines.size();
				if(i>0){
					builder.append(" ");
				}
				if (height > index) {
					builder.append(cell.lines.get(index));
				} else {
					builder.append(padding(cell.width));
				}
				/// System.out.println(cell.lines);
				if (index < height-1) {
					BREAK = false;
				}
			}
			builder.append(System.lineSeparator());
			index++;
			if (BREAK) {
				break;
			}
		}
		return builder.toString();
	}

	/*
	 * public String format(Cell cell) {
	 * 
	 * if (cell == null) { return ""; } StringBuilder b = new StringBuilder();
	 * String text = ""; if (cell.text != null && !cell.text.trim().isEmpty()) {
	 * text = cell.text.trim(); }
	 * 
	 * if (cell.width < 1) { return text; }
	 * 
	 * if (text.length() <= cell.width) { if (ALIGN.LEFT.equals(cell.align)) {
	 * b.append(text); b.append(padding(cell.width - text.length())); } else {
	 * b.append(padding(cell.width - text.length())); b.append(text); } } else {
	 * b.append(WordUtils.wrap(text, cell.width)); }
	 * 
	 * return b.toString(); }
	 */

	private String padding(int length) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < length; i++) {
			b.append(" ");
		}
		return b.toString();
	}

	class Cell {
		private List<String> lines = new ArrayList<String>();
		private int row;
		private int column;

		private int width;
		private ALIGN align;

		public Cell(int row, int column, String text, ALIGN align, int width) {
			this.row = row;
			this.column = column;
			this.width = width;
			this.align = align;

			if (text != null) {
				if (text.trim().length() > width) {
					String p = WordUtils.wrap(text.trim(), width);
					String[] lines = StringUtils.split(p, System.lineSeparator());
					for (String line : lines) {
						this.lines.add(format(line, align, width));
					}
				} else {
					lines.add(format(text, align, width));
				}
			}

		}

		private String format(String text, ALIGN align, int width) {
			String line = null;
			if (ALIGN.LEFT.equals(align)) {
				line = text + padding(width - text.length());
			} else {
				line = padding(width - text.length()) + text;
			}
			return line;
		}
	}

	enum ALIGN {
		LEFT, RIGHT;
	};
}
