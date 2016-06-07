package edu.iris.dms.table.view.console;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import edu.iris.dms.table.Cell;
import edu.iris.dms.table.Column;
import edu.iris.dms.table.Index;
import edu.iris.dms.table.Row;
import edu.iris.dms.table.SupportUtil;
import edu.iris.dms.table.Table;
import edu.iris.dms.table.view.AbstractRenderer;
import edu.iris.dms.table.view.CellRenderer;
import edu.iris.dms.table.view.Renderer;
import edu.iris.dms.table.view.Style;

public class ConsoleTableRenderer extends AbstractRenderer<Table> {

	private String columnSeperator = ":";
	private String rowSeperator = "-";
	private int spacing = 1;

	private String TOP_LEFT_JOINT = "┌";
	private String TOP_RIGHT_JOINT = "┐";
	private String BOTTOM_LEFT_JOINT = "└";
	private String BOTTOM_RIGHT_JOINT = "┘";
	private String TOP_JOINT = "┬";
	private String BOTTOM_JOINT = "┴";
	private String LEFT_JOINT = "├";
	private String JOINT = "┼";
	private String RIGHT_JOINT = "┤";
	private char HORIZONTAL_LINE = '─';
	private char PADDING = ' ';
	private String VERTICAL_LINE = "│";

	public static String CELL_EDGE = "|";

	private Renderer<Cell> defaultCellRenderer;

	public ConsoleTableRenderer(PrintStream stream) {
		this(stream, new BasicTableStyle());
	}

	public ConsoleTableRenderer(PrintStream stream, Style style) {
		super(stream, style);
		defaultCellRenderer = new CellRenderer(stream, style);
	}

	@Override
	public void render(Table table) {

		List<Row> rows = prepare(table);
		int group = 0;
		int rowWidth = table.getWidth();
		if (table.getBorder() > 0) {
			this.getStream().print(LEFT_JOINT);
			for (int i = 0; i < rowWidth + (table.getColumns().size() - 1); i++) {
				this.getStream().print(ConsoleTableStyle.HORIZONTAL_LINE);
			}
			this.getStream().print(RIGHT_JOINT);
			this.getStream().println();
		}
		for (Row r : rows) {

			if (group != r.getGroup()) {
				if (table.getBorder() > 0) {
					if (table.getBorder() > 0) {
						this.getStream().print(ConsoleTableStyle.LEFT_JOINT);
					}
					for (int i = 0; i < rowWidth + (table.getColumns().size() - 1); i++) {
						this.getStream().print(ConsoleTableStyle.HORIZONTAL_LINE);
					}
					if (table.getBorder() > 0) {
						this.getStream().print(ConsoleTableStyle.RIGHT_JOINT);
					}
					this.getStream().println();
				}
				group = r.getGroup();
			}

			for (int i = 0; i < r.getCells().size(); i++) {
				if (table.getBorder() > 0) {
					this.getStream().print(ConsoleTableStyle.VERTICAL_LINE);
				}
				Cell cell = r.getCells().get(i);
				Column column = table.getColumn(cell.getIndex().getColumn());
				cell.setWidth(column.getWidth());
				this.getCellRenderer().render(cell);
			}
			if (table.getBorder() > 0) {
				this.getStream().print(ConsoleTableStyle.VERTICAL_LINE);
			}
			this.getStream().println();
		}
	}

	private List<Row> prepare(Table table) {
		List<Row> list = new ArrayList<Row>();
		for (Row row : table.getRows()) {
			List<Row> rows = prepareRow(table, row);
			list.addAll(rows);
		}
		return list;
	}

	private List<Row> prepareRow(Table table, Row row) {
		List<Row> list = new RowList(row.getIndex());
		for (int i = 0; i < row.getCells().size(); i++) {
			Cell cell = row.getCells().get(i);
			int width = table.getColumn(i).getWidth();
			Column theColumn = table.getColumn(i);
			if (theColumn.getWidth() > 0 && cell.getData().length() > theColumn.getWidth()) {
				List<Cell> l = SupportUtil.wrap(cell.getData(), width);
				for (int x = 0; x < l.size(); x++) {
					Cell c = l.get(x);
					c.setIndex(new Index(row.getIndex(), i));
					c.setAlignment(cell.getAlignment());
					c.setPadding(cell.getPadding());
					list.get(x).add(c);
				}
			} else {
				list.get(0).add(cell);
			}
		}
		return list;
	}

	private void rowSeperator(int width) {
		this.getStream().print("+");
		for (int i = 0; i < width; i++) {
			this.getStream().print(this.style.getRowSeperator());
		}
		this.getStream().print("+");
		this.getStream().println();
	}

	private String printSpaces(int count) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < count; ++i) {
			builder.append(" ");
		}
		return builder.toString();
	}

	public Renderer<Cell> getCellRenderer() {
		return this.defaultCellRenderer;
	}

	class RowList extends ArrayList<Row> {

		private int group;

		public RowList(int group) {
			super();
			this.group = group;
		}

		@Override
		public Row get(int index) {
			if (index < this.size()) {
				return super.get(index);
			}
			Row row = new Row(index);
			row.setGroup(this.group);
			this.add(row);
			return row;
		}

		public int getGroup() {
			return group;
		}

	}
}
