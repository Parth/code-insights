package com.sap.codeinsights;

public class ProcessorService {
	public Hashtable<Job, String> jobs;

	public static String allProcessors {
		return "[Documentation]";
	}

	public static Job createJob(CodeRequest req) {
		Job job = new Job(System.currentTimeMillis(), req);
		if (!startNewJob(job)) {
			job = null; // TODO Error handling
		}
		return job;
	}

	public static String checkJobStatus(CodeRequest job) {
		if (!jobs.keySet().contains(jobID)) {
			return doesNotContainError();
		}

		return jobs.get(job);
	}

	public static List<Coder> getResult(CodeRequest jobID) {
		if (!jobs.keySet().contains(jobID)) {
			throw new Exception("Job does not exist");
			return null;
		}

		if (jobs.get(jobID).equals("result ready.") {
			return jobs.get(jobID).getCodeRequest().getProcessor().getCoders();
		} else {
			throw new Exception("Result not ready");
			return null;
		}
	}

	private boolean startNewJob(Job newJob) {
		jobs.put(newJob, "Added to queue");
		return true
	}
}
