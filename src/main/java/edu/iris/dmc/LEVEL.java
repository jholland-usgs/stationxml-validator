package edu.iris.dmc;

public enum LEVEL {

	NETWORK("network", 1), STATION("station", 2), CHANNEL("channel", 3), RESPONSE("response", 4);

	private int value;
	private String name;

	private LEVEL(int value) {
		this(null, value);
	}

	private LEVEL(String name, int value) {
		this.value = value;
		this.name = name;
	}

	public int getValue() {
		return this.value;
	}

	public static LEVEL parse(String value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		if ("network".equalsIgnoreCase(value) || "net".equalsIgnoreCase(value)) {
			return NETWORK;
		}
		if ("station".equalsIgnoreCase(value) || "sta".equalsIgnoreCase(value)) {
			return STATION;
		}
		if ("channel".equalsIgnoreCase(value) || "cha".equalsIgnoreCase(value)) {
			return CHANNEL;
		}
		if ("response".equalsIgnoreCase(value) || "resp".equalsIgnoreCase(value)) {
			return RESPONSE;
		}
		throw new IllegalArgumentException();
	}
}

