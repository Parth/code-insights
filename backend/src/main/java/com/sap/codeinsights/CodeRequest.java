package com.sap.codeinsights;

import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class CodeRequest {
	private String url;
	private String processorType;
	private List<Coder> result;

	public CodeRequest(String url, String processorType) {
		this.url = url;
		this.processorType = processorType;
	}

	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public String getProcessorType() {
		return processorType;
	}

	public void setProcessorType(String processorType) {
		this.processorType = processorType;
	}

    public JsonObject toJson() {
        JsonParser parser = new JsonParser();
        return parser.parse(this.toString()).getAsJsonObject();
    }

	public void setResult(List<Coder> result) {
		this.result = result;
	}

	public List<Coder> getResult() {
		return result;
	}

	@Override
	public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

	@Override
	public boolean equals(Object o) {
		CodeRequest cr = (CodeRequest) o;

		return cr.getURL().equalsIgnoreCase(this.getURL()) && 
			cr.getProcessorType().equalsIgnoreCase(this.getProcessorType());
	}
	
	@Override
	public int hashCode() {
		String hash = url + ":" + processorType;
		return hash.hashCode();
	}
}
