package edu.iris.dmc.station.validator;

import edu.iris.dmc.Application;

public class ApplicationTest {

	public static void main(String[] args) throws Exception {
		args = new String[] {"/Users/Suleiman/PROJECTS/StationXML-Validator/validation-client/src/test/resources/XX-TEST-FalseFailure-Rule413.xml.txt"};
		//args = new String[] {"/Users/Suleiman/stations/archive/", "--summary", "-o=/Users/Suleiman/outs.txt"};
		args = new String[] {"--print-rules"};
		//args = new String[] {"--version"};
		//args = new String[] {"/Users/Suleiman/PROJECTS/StationXML-Validator/validation-client/src/test/resources/co_csb.xml","--ignore-warnings"};
		
		Application.main(args);
	}
}
