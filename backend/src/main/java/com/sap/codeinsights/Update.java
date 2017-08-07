package com.sap.codeinsights;

public class Update {
	public int code;
	public String update;

	public Update(int code, String update) {
		this.code = code;
		this.update = update;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Update update1 = (Update) o;

		if (code != update1.code) return false;
		return update.equals(update1.update);
	}

	@Override
	public int hashCode() {
		int result = code;
		result = 31 * result + update.hashCode();
		return result;
	}
}
