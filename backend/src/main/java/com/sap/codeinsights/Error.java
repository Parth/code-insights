package com.sap.codeinsights;

public class Error {
	public final String message; 
	public final int errorNumber;

	public Error(String message) {
		this.errorNumber = 1;
		this.message = message;
	}

	public Error(String message, int errorNumber) {
		this.errorNumber = errorNumber;
		this.message = message;
	}

    public JsonObject toJson() {
        JsonParser parser = new JsonParser();
        return parser.parse(this.toString()).getAsJsonObject();
    }

	@Override
	public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
