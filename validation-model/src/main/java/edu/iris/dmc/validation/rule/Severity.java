package edu.iris.dmc.validation.rule;

public enum Severity {
	WARN("WARNING"), ERROR("ERROR");

	private String description;

	private Severity(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
