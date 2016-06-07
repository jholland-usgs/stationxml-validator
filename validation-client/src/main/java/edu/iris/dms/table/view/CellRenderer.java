package edu.iris.dms.table.view;

import java.io.PrintStream;

import edu.iris.dms.table.Cell;
import edu.iris.dms.table.SupportUtil;

public class CellRenderer extends AbstractRenderer<Cell> {

	public CellRenderer(PrintStream stream) {
		super(stream);
	}

	public CellRenderer(PrintStream stream, Style style) {
		super(stream, style);
	}

	@Override
	public void render(Cell cell) {
		this.getStream().print(SupportUtil.format(cell));

	}

}
