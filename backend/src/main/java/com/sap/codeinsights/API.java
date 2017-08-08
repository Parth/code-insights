package com.sap.codeinsights;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class API {
	public static String createJob(String json) {
		Gson gson = new Gson(); 
		CodeRequest request = gson.fromJson(json, CodeRequest.class);
		return ProcessorService.createJob(request).toString();
	}

	public static String checkJobStatus(String json) {
		Gson gson = new Gson(); 
		Job job = gson.fromJson(json, Job.class);
		return ProcessorService.getStatus(job).getCurrentStatus();
	}

	public static String getJobLog(String json) {
		Gson gson = new Gson(); 
		Job job = gson.fromJson(json, Job.class);
		return ProcessorService.getStatus(job).getStatusLog();
	}

	//TODO handle exception
	public static String getJobResult(String json) throws Exception {
		Gson gson = new Gson(); 
		Job job = gson.fromJson(json, Job.class);
		return ProcessorService.getResult(job).toString();
	}

	public static String processorsAvailable() {
		return ProcessorService.allProcessors().toString();
	}
}
