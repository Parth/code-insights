package com.sap.codeinsights;

import java.util.Hashtable;
import java.util.List;

public class ProcessorService {
	public static Hashtable<Job, Status> jobs = new Hashtable<Job, Status>();

	public static String allProcessors() {
		// TODO should be creating a JSONArray or something similar
		return "[Documentation]";
	}

	//TODO Don't repeat a job request that's already being worked on
	public static Job createJob(CodeRequest req) throws Error {
		Job job = new Job(System.currentTimeMillis(), req);
		if (alreadyRunning(req)) {
			throw new Error("That request is already being processed. Use /check-job to check it's status.", Error.JOB_ALREADY_STARTED);
		}

		if (!startedSuccessfully(job)) {
			System.err.println("job is not alive");
		}
		return job;
	}

	// TODO Save log to db and retreive if not done
	public static Status getStatus(Job job) throws Error {
		if (!jobs.containsKey(job)) {
			throw new Error("Job does not exist.", Error.JOB_NOT_FOUND);
		}

		return jobs.get(job);
	}

	public static List<Coder> getResult(Job job) throws Error {
		if (!jobs.containsKey(job)) {
			throw new Error("Job does not exist.", Error.JOB_NOT_FOUND);
		}

		if (jobs.get(job).getStatusCode() == 1) {
			return job.getCodeRequest().getResult();
		} else {
			throw new Error("Job not complete.", Error.JOB_NOT_DONE);
		}
	}

	// TODO: Starting the same processor on the same url will cause issues as they'll both try to modify the same files while they clone the repo. Prevent the same code requests from getting run. And make sure that processors don't write to the disk.
	private static boolean startedSuccessfully(Job newJob) {
		Runnable task = () -> {
			Status s = new Status("Request Created");
			s.setStatusCode(0);
			jobs.put(newJob, s);
			RepositoryProcessor.process(newJob.getCodeRequest(), (update) -> {
				jobs.get(newJob).pushUpdate(update);
			});
		};

		Thread t = new Thread(task);
		t.start();
		return t.isAlive();
	}

	private static boolean alreadyRunning(CodeRequest req) {
		return jobs.values().contains(req);
	}
}
