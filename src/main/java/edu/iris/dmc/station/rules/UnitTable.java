package edu.iris.dmc.station.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UnitTable {

	public static List<String> units = new ArrayList<String>(Arrays.asList("meter", "meters", "m", "m/s", "m/s**2",
			"centimeter", "centimeters", "cm", "cm/s", "cm/s**2", "millimeter", "millimeters", "mm", "mm/s", "mm/s**2",
			"mm/hour", "micrometer", "micrometers", "um", "um/s", "um/s**2", "nanometer", "nm", "nm/s", "nm/s**2",
			"second", "seconds", "nanometers", "nanoradians", "bars", "s", "millisecond", "ms", "microsecond", "us",
			"nanosecond", "nanoseconds", "ns", "minute", "minutes", "min", "hour", "hours", "radian", "radians", "rad",
			"microradian", "microradians", "urad", "nanoradian", "nrad", "rad/s", "rad/s**2", "degree", "degrees",
			"deg", "kelvin", "K", "celsius", "degC", "candela", "cd", "pascal", "pascals", "Pa", "kilopascal",
			"kilopascals", "kPa", "hectopascal", "hectopascals", "hPa", "bar", "millibar", "millibars", "mbar",
			"ampere", "amperes", "A", "milliamp", "milliamps", "mA", "volt", "volts", "V", "millivolt", "millivolts",
			"mV", "microvolt", "microvolts", "uV", "ohm", "hertz", "Hz", "newton", "newtons", "N", "joule", "joules",
			"J", "tesla", "T", "nanotesla", "nT", "strain", "m/m", "m**3/m**3", "cm/cm", "mm/mm", "um/um", "nm/nm",
			"microstrain", "watt", "watts", "W", "milliwatt", "milliwatts", "mW", "V/m", "W/m**2", "gap", "reboot",
			"byte", "bytes", "bit", "bit/s", "percent", "%", "count", "counts", "number", "unitless", "unknown",
			"UNKNOWN"));

	public static boolean contains(String name) {
		boolean bool = units.contains(name);
		return bool;
	}

	public static boolean containsCaseInsensitive(final String name) {
		for (String s : units) {
			if (s.toLowerCase().equals(name.toLowerCase())) {
				return true;
			}

		}
		return false;
	}
}
