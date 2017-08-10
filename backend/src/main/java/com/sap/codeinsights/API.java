package com.sap.codeinsights;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class API {
	public static String createJob(String json) {
		Gson gson = new Gson(); 
		CodeRequest request = gson.fromJson(json, CodeRequest.class);

		if (CodeRequest.getValidity(request) != null) {
			return CodeRequest.getValidity(request).toString();
		}

		try {
			return ProcessorService.createJob(request).toString();
		} catch (Error e) {
			return e.toString();
		}
	}

	public static String checkJobStatus(String json) {
		Gson gson = new Gson(); 
		Job job = gson.fromJson(json, Job.class);
		if (Job.getValidity(job) != null) {
			return Job.getValidity(job).toString();
		}

		try {
			return ProcessorService.getStatus(job).toString();
		} catch (Error e) {
			return e.toString();
		}
	}

	public static String getJobLog(String json) {
		Gson gson = new Gson(); 
		Job job = gson.fromJson(json, Job.class);
		if (Job.getValidity(job) != null) {
			return Job.getValidity(job).toString();
		}

		try {
			return ProcessorService.getStatus(job).getStatusLog();
		} catch (Error e) {
			return e.toString();
		}
	}

	public static String getJobResult(String json) {
		Gson gson = new Gson(); 
		Job job = gson.fromJson(json, Job.class);
		if (Job.getValidity(job) != null) {
			return Job.getValidity(job).toString();
		}

		try {
			return ProcessorService.getResult(job).toString();
		} catch (Error e) {
			return e.toString();
		}
	}

	public static String processorsAvailable() {
		return ProcessorService.allProcessors().toString();
	}
}
