package com.sap.codeinsights;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class API {
	public static String processessRepository(String url) {
		 return RepositoryProcessor.process(url);
	}

	public static String processorsAvailable() {
		return ProcessorService.allProcessors();
	}

	public static String newJob() {
		return ProcessorService.createJob(
	}
}
