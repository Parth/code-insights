package com.sap.codeinsights;

import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class Job {
	private long jobID;
	private CodeRequest request;

	public Job(int jobID, CodeRequest request) {
		this.jobID = jobID;
		this.request = request;
	}

	public int getJobId() {
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

	@Override
	public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

	@Override
	public int hashCode() {
		return Long.hashCode(jobID);
	}
}
