package edu.iris.dmc.validation.validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

import edu.iris.dmc.validation.rule.Rule;
import edu.iris.dmc.validation.rule.UnitTable;


public class Util {

	public static List<Rule> rules() {

		List<Rule> list = new ArrayList<Rule>();
		PlatformResourceBundleLocator bundleLocator = new PlatformResourceBundleLocator(
				ResourceBundleMessageInterpolator.USER_VALIDATION_MESSAGES);
		ResourceBundle resourceBundle = bundleLocator.getResourceBundle(Locale.US);
		Enumeration<String> keys = resourceBundle.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value = resourceBundle.getString(key);
			String[] array = value.split(",", 2);
			list.add(new Rule(Integer.valueOf(array[0]), key, array[1].trim()));
		}
		Collections.sort(list);
		return list;
	}

	public static UnitTable units() {
		return UnitValidator.unitTable;
	}

	public static void main(String[] args) {
		int gaps = 2;
		int col1 = 15;
		int col2 = 40;
		System.out.printf("%" + col1 + "s %2s %-40s %n", "Number", "|", "Description");
	}
}
