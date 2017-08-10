package com.sap.codeinsights;

import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class Job {
	private long jobID;
	private CodeRequest request;

	public Job(long jobID, CodeRequest request) {
		this.jobID = jobID;
		this.request = request;
	}

	public long getJobId() {
		return jobID;
	}

	public void setJobId(int jobID) {
		this.jobID = jobID;
	}

	public CodeRequest getCodeRequest() {
		return request;
	}

	public void setCodeRequest() {
		this.request = request;
	}

    public JsonObject toJson() {
        JsonParser parser = new JsonParser();
        return parser.parse(this.toString()).getAsJsonObject();
    }

	public static Error getValidity(Job j) {
		if (j == null) return new Error("Null Job.", Error.NULL_ERROR);

		if (j.getCodeRequest() == null) return new Error("Missing Code Request");

		// TODO clean this up
		if (CodeRequest.getValidity(j.getCodeRequest()) != null) return CodeRequest.getValidity(j.getCodeRequest());
		return null;
	}

	@Override
	public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

	@Override
	public int hashCode() {
		return Long.hashCode(jobID);
	}

	@Override
	public boolean equals(Object o) {
		Job j = (Job) o;
		return j.getJobId() == jobID && j.getCodeRequest().equals(j.getCodeRequest());
	}
}
