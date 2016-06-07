package edu.iris.dms.table;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.WordUtils;

import edu.iris.dms.table.view.ALIGN;

public class SupportUtil {

	public static String format(Cell cell) {
		return SupportUtil.format(cell.getData(), cell.getWidth(), cell.getAlignment(), cell.getPadding());
	}

	public static String format(String text, int width, ALIGN align, int padding) {
		if (ALIGN.RIGHT.equals(align)) {
			return alignRight(text, width, padding);
		} else {
			return alignLeft(text, width, padding);
		}
	}

	public static String alignRight(String s, int width, int padding) {
		if (s == null) {
			return null;
		}

		if (s.length() >= width) {
			return s;
		}

		String theString = s.trim();

		if (padding >= 0) {
			for (int i = 0; i < padding; i++) {
				theString = theString + " ";
			}
		}

		int length = width - theString.length();
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				theString = " " + theString;
			}
		}

		return theString;
	}

	// pad with " " to the left to the given length (n)
	public static String alignLeft(String s, int width, int padding) {
		if (s == null) {
			return null;
		}

		if (s.length() >= width) {
			return s;
		}

		String theString = s.trim();

		if (padding >= 0) {
			for (int i = 0; i < padding; i++) {
				theString = " " + theString;
			}
		}

		int length = width - theString.length();

		if (length > 0) {
			for (int i = 0; i < length; i++) {
				theString = theString + " ";
			}
		}

		return theString;
	}

	public static List<Cell> wrap(String text, int width) {
		if (text == null) {
			return null;
		}
		String string = WordUtils.wrap(text, width);
		BufferedReader bufReader = new BufferedReader(new StringReader(string));
		String line = null;
		List<Cell> lines = new ArrayList<Cell>();
		try {
			while ((line = bufReader.readLine()) != null) {
				lines.add(CellFactory.create(line.trim()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}
	public static void main(String[] args){
		System.out.println("["+alignRight(" ",10, 1)+"]");
		System.out.println("["+alignRight("412",10, 1)+"]");
	}
	
}
