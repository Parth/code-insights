package com.sap.codeinsights;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class API {

	//TODO StatusCode? 
	public static String createJob(String json) {
		Gson gson = new Gson(); 
		CodeRequest request = gson.fromJson(json, CodeRequest.class);
		return ProcessorService.createJob().toString();
	}

	public static String checkJobStatus(String json) {
		Gson gson = new Gson(); 
		CodeRequest request = gson.fromJson(json, CodeRequest.class);
		return ProcessorService.checkJobStatus().toString();
	}

	public static String getJobResult(String json) {
		Gson gson = new Gson(); 
		CodeRequest request = gson.fromJson(json, CodeRequest.class);
		return ProcessorService.getResult().toString();
	}

	public static String processorsAvailable() {
		return ProcessorService.allProcessors().toString();
	}
}
