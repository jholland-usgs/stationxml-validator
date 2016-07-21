package edu.iris.dmc.station.validator;

import edu.iris.dmc.Application;

public class ApplicationTest {

	public static void main(String[] args) throws Exception {
		args = new String[] {"/Users/Suleiman/PROJECTS/StationXML-Validation/validation-client/src/test/resources/IIKDAK10VHZ_nullLOCCODE.xml"};
		//args = new String[] {"/Users/Suleiman/stations/archive/", "--summary", "-o=/Users/Suleiman/outs.txt"};
		//args = new String[] {"--print-rules"};
		Application.main(args);
	}
}
