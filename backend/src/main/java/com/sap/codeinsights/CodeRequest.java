package com.sap.codeinsights;

import java.util.List;

import org.apache.commons.validator.routines.UrlValidator;

import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class CodeRequest {
	private String url;
	private String processorType;

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

	public static Error getValidity(CodeRequest o) {
		if (o == null) return new Error("Null Code Request.", Error.NULL_ERROR);

		//TODO this is a bit unclear, make it more clear;
		// TODO check for domains that are more than 3 characters like .corp
		String url = o.getURL();
		String processorType = o.getProcessorType();

		if (url == null || url.trim().isEmpty()) return new Error("No URL Provided", Error.MISSING_URL);

		UrlValidator urlValidator = new UrlValidator();
		if (!urlValidator.isValid(url)) return new Error("Invalid URL", Error.INVALID_URL);

		if (!processorType.equalsIgnoreCase("documentationprocessor") && !processorType.equalsIgnoreCase("blameprocessor")) return new Error("Invalid Processor Type", Error.INVALID_PROCESSOR);

		return null;
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
