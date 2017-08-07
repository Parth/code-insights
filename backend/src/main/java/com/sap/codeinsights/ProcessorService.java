package com.sap.codeinsights;

import java.util.Hashtable;
import java.util.List;

public class ProcessorService {
	public static Hashtable<Job, Status> jobs = new Hashtable<Job, Status>();

	public static String allProcessors() {
		// TODO should be creating a JSONArray or something similar
		return "[Documentation]";
	}

	public static Job createJob(CodeRequest req) {
		Job job = new Job(System.currentTimeMillis(), req);
		if (!startNewJob(job)) {
			System.err.println("job is not alive");
		}
		return job;
	}

	// TODO Save log to db and retreive if not done
	public static Status getStatus(Job job) {
		if (!jobs.containsKey(job)) {
			return null;
		}

		return jobs.get(job);
	}

	public static List<Coder> getResult(Job job) throws Exception {
		if (!jobs.contains(job)) {
			throw new Exception("Job does not exist");
		}

		if (jobs.get(job).getStatusCode() == 1) {
			return job.getCodeRequest().getResult();
		} else {
			throw new Exception("Result not ready");
		}
	}

	private static boolean startNewJob(Job newJob) {
		Runnable task = () -> {
			Status s = new Status("Request Created");
			s.setStatusCode(0);
			jobs.put(newJob, s);
			RepositoryProcessor.process(newJob.getCodeRequest(), (update) -> {
				System.out.println(update);
				jobs.get(newJob).pushUpdate(update);
			});
		};

		Thread t = new Thread(task);
		t.start();
		return t.isAlive();
	}
}
