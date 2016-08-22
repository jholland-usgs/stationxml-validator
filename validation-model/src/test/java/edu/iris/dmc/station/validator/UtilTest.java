package edu.iris.dmc.station.validator;

import java.util.List;

import edu.iris.dmc.validation.rule.Rule;
import edu.iris.dmc.validation.validator.Util;

public class UtilTest {

	public static void main(String[] args) {
		List<Rule> rules = Util.rules();
		
		for(Rule r:rules){
			System.out.println(r.getId()+"   "+r.getMessage());
		}

	}

}
