package edu.iris.dmc.station.validator;

import edu.iris.dmc.Application;

public class ApplicationTest {

	public static void main(String[] args) {
		args = new String[] {"--print-rules"};
		//args = new String[] {"/Users/Suleiman/stations/archive/", "--summary", "-o=/Users/Suleiman/outs.txt"};
		
		Application.main(args);
	}
}
