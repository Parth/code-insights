package com.sap.codeinsights;

import java.util.Hashtable;
import java.util.List;

public class ProcessorService implements Updatable {
	public static Hashtable<Job, String> jobs;

	public static String allProcessors() {
		return "[Documentation]";
	}

	public static Job createJob(CodeRequest req) {
		Job job = new Job(System.currentTimeMillis(), req);
		if (!startNewJob(job)) {
			job = null; // TODO Error handling
		}
		return job;
	}

	public static String checkJobStatus(Job job) throws Exception {
		if (!jobs.contains(job)) {
			throw new Exception("Job does not exist");
		}

		return jobs.get(job);
	}

	public static List<Coder> getResult(Job job) throws Exception {
		if (!jobs.contains(job)) {
			throw new Exception("Job does not exist");
		}

		if (jobs.get(job).equals("result ready.")) {
			return job.getCodeRequest().getProcessor().getCoders();
		} else {
			throw new Exception("Result not ready");
		}
	}

	private static boolean startNewJob(Job newJob) {
		jobs.put(newJob, "Added to queue");
		return true;
	}

	@Override
	public void pushUpdate(String update) {
		
	}
}
