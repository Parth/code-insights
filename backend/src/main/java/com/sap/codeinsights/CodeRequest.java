package com.sap.codeinsights;

import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class CodeRequest {
	private String url;
	private Processor processor;

	public CodeRequest(String url, Processor processor) {
		this.url = url;
		this.processor = processor;
	}

	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public Processor getProcessor() {
		return processor;
	}

	public void setProcessor(Processor processor) {
		this.processor = processor:
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
	public boolean equals(Object o) {
		CodeRequest cr = (CodeRequest) o;

		return cr.getURL().equalsIgnoreCase(this.getURL()) && 
			cr.processor.getType().equalsIgnoreCase(this.getProcessor().getType());
	}
	
	@Override
	public int hashCode() {
		String hash = url + ":" + processor.getType();
		return hash.hashCode();
	}
}
