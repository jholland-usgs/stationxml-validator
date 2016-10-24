package edu.iris.dmc.validation.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UnitTable {
	private String tag = "2016-05-06";
	private List<String> units = new ArrayList<String>(Arrays.asList("meter", "m", "m/s", "m/s**2",
			"centimeter", "cm", "cm/s", "cm/s**2",
			"millimeter", "mm", "mm/s", "mm/s**2", "mm/hour",
			"micrometer", "um", "um/s", "um/s**2",
			"nanometer", "nm", "nm/s", "nm/s**2",
			"second", "s", "millisecond", "ms", "microsecond", "us", "nanosecond", "ns",
			"minute", "min",
			"hour",
			"radian", "rad", "microradian", "urad", "nanoradian", "nrad",
			"rad/s", "rad/s**2",
			"degree", "deg",
			"kelvin", "K",
			"celsius", "degC",
			"candela", "cd",
			"pascal", "Pa", "kilopascal", "kPa", "hectopascal", "hPa",
			"bar", "millibar", "mbar",
			"ampere", "A", "milliamp", "mA",
			"volt", "V", "millivolt", "mV", "microvolt", "uV",
			"ohm",
			"hertz", "Hz",
			"newton", "N",
			"joule", "J",
			"tesla", "T", "nanotesla", "nT",
			"strain", "m/m", "m**3/m**3", "cm/cm", "mm/mm", "um/um", "nm/nm", "microstrain",
			"watt", "W", "milliwatt", "mW",
			"V/m",
			"W/m**2",
			"gap",
			"reboot",
			"byte","bit",
			"bit/s",
			"percent","%",
			"count","counts",
			"number",
			"unitless"));

	public String getTag() {
		return tag;
	}

	public List<String> getUnits() {
		return units;
	}

	public boolean contains(String name) {
		return this.units.contains(name);
	}

	public boolean containsCaseInsensitive(String name) {
		if(name==null){
			
		}
		name=name.toLowerCase();
		return this.units.contains(name);
	}
}
