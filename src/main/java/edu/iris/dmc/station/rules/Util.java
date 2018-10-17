package edu.iris.dmc.station.rules;

public class Util {



	public static boolean equal(double a, double b) {
		return (Math.abs(a - b)/a) < Double.valueOf(0.05);
	}
}
