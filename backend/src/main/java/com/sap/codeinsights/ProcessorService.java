package com.sap.codeinsights;

public class ProcessorService {
	public Hashtable<Job, String> jobs;

	public static String allProcessors {
		return "[Documentation]";
	}

	public static Job createJob(CodeRequest req) {
		Job job = new Job(System.currentTimeMillis(), req);
		return job;
	}

	public static String checkJobStatus(CodeRequest job) {
		if (!jobs.keySet().contains(jobID)) {
			return doesNotContainError();
		}

		return jobs.get(job);
	}

	public String doesNotContainError() {
		return "{\"error\": \"Job does not exist\"}";
	}

	public static String getResult(CodeRequest jobId) {
		goto db; 
		query; 
		return result;
	}
}
