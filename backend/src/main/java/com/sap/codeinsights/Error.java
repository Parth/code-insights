package com.sap.codeinsights;

import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

// TODO perhaps rename to API error to make clear it's distinction from Exception
public class Error extends Exception {
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

	@Override
	public String toString() {
		JsonObject json = new JsonObject();
		json.addProperty("message", message);
		json.addProperty("errorNumber", errorNumber);

        JsonObject error = new JsonObject();
		error.add("error", json);
		return error.toString();
    }

	public static final int MISSING_URL = 1;
	public static final int INVALID_URL = 2;
	public static final int INVALID_PROCESSOR = 3;
	public static final int NULL_ERROR = 4;
	public static final int JOB_NOT_FOUND = 5;
	public static final int JOB_NOT_DONE = 6;
	public static final int JOB_ALREADY_STARTED = 7;
}
