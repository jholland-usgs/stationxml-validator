package edu.iris.dms.table.view;

public enum ALIGN {
	LEFT("-"), RIGHT("+"), CENTER("");

	private String value;

	private ALIGN(String s) {
		this.value = s;
	}

	public String getValue() {
		return value;
	}

}
