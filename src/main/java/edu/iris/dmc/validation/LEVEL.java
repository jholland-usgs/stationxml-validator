package edu.iris.dmc.validation;

public enum LEVEL {

	NETWORK("net", 1), STATION("sta", 2), CHANNEL("cha", 3), RESPONSE("resp", 4);

	private String text;
	private int value;

	private LEVEL(String text, int value) {
		this.text = text;
		this.value = value;
	}

	public static LEVEL parse(String value) {
		switch (value.toLowerCase()) {
		case "net":
		case "network":
			return NETWORK;
		case "sta":
		case "station":
			return STATION;
		case "cha":
		case "channel":
			return CHANNEL;
		case "resp":
		case "response":
			return RESPONSE;
		default:
			throw new IllegalArgumentException(value + " is not a valid!");
		}
	}

	public static LEVEL parse(int value) {
		switch (value) {
		case 1:
			return NETWORK;
		case 2:
			return STATION;
		case 3:
			return CHANNEL;
		case 4:
			return RESPONSE;
		default:
			throw new IllegalArgumentException(value + " is not a valid!");
		}
	}

	public String getText() {
		return text;
	}

	public int getValue() {
		return value;
	}
}
