package edu.iris.dmc.validation.rule;

public class Rule implements Comparable<Rule> {

	private int id;
	private String key;
	private String message;

	public Rule(int id, String key, String message) {
		super();
		this.id = id;
		this.key = key;
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public int compareTo(Rule o) {
		if (this.id > o.id) {
			return 1;
		}
		if (this.id < o.id) {
			return -1;
		}
		return 0;
	}

}
