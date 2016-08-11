package edu.iris.dmc.validation.rule;

import java.util.Arrays;
import java.util.List;

public class UnitTable {
	private String tag = "2016-05-06";
	private List<String> units = Arrays.asList("METER", "M", "MILLIMETER", "NANOMETER", "M/S", "M/S**2", "SECOND", "S",
			"MICROSECOND", "NANOSECOND", "RADIAN", "RAD", "MICRORADIAN", "NANORADIAN", "RAD/S", "RAD/S**2", "KELVIN",
			"K", "CELSIUS", "CANDELA", "CD", "PASCAL", "PA", "KILOPASCAL", "HECTOPASCAL", "BAR", "MILLIBAR", "AMPERE",
			"A", "VOLT", "V", "OHM", "HERTZ", "HZ", "NEWTON", "N", "JOULE", "J", "TESLA", "T", "NANOTESLA", "STRAIN",
			"M/M", "MICROSTRAIN", "GAP", "REBOOT", "BIT", "BYTE", "PERCENT", "COUNT", "UNITLESS");

	public String getTag() {
		return tag;
	}

	public List<String> getUnits() {
		return units;
	}

	public boolean contains(String name) {
		return this.units.contains(name);
	}
}
