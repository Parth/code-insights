package com.sap.codeinsights;

import java.util.Hashtable;
import java.util.List;

public class ProcessorService {
	public static final Hashtable<Job, Status> jobs = new Hashtable<Job, Status>();
	public static final Hashtable<Job, Object> results = new Hashtable<Job, List<DocumentationCoder>>();

	public static String allProcessors() {
		// TODO should be creating a JSONArray or something similar
		return "[Documentation]";
	}

	public synchronized static Job createJob(CodeRequest req) throws Error {
		Job job = new Job(System.currentTimeMillis(), req);
		if (findJobByRequest(req) != null) {
			if (jobs.get(findJobByRequest(req)).getStatusCode() == 1) {
				jobs.remove(findJobByRequest(req));
			} else {
				throw new Error("That request is already being processed. Use /check-job to check it's status.", Error.JOB_ALREADY_STARTED);
			}
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

	public static List<DocumentationCoder> getResult(Job job) throws Error {
		if (!jobs.containsKey(job)) {
			throw new Error("Job does not exist.", Error.JOB_NOT_FOUND);
		}

		if (jobs.get(job).getStatusCode() == 1) {
			List<DocumentationCoder> ret = results.get(job);
			jobs.remove(job);
			return ret;
		} else {
			throw new Error("Job not complete.", Error.JOB_NOT_DONE);
		}
	}

	private static boolean startedSuccessfully(Job newJob) {
		Status s = new Status("Request Created");
		s.setStatusCode(0);
		jobs.put(newJob, s);
		Runnable task = () -> {
			results.put(newJob, getResult(job));
		};

		Thread t = new Thread(task);
		t.start();
		return t.isAlive();
	}

	private static void getResult(Job job) {
		Updateable simpleUpdate = (update) -> {
			jobs.get(newJob).pushUpdate(update);
		};

		Resultable simpleResult = (result) -> {
			results.put(job, result);
		};

		CodeRequest r = job.getCodeRequest();

		switch(r.getProcessorType().toLowercase()) {
			case Processor.getType().toLowercase():
				return new DocumentationProcessor(job.getCodeRequest(), simpleUpdate).getResult(simpleResult);

			//case BlameProcessor.getType().toLowercase():
			//	return new BlameProcessor(job.getCodeRequest(), simpleUpdate).getResult(simpleResult);

			default:
				return null;
		}
	}

	private static Job findJobByRequest(CodeRequest req) {
		for (Job j : jobs.keySet()) {
			if (j.getCodeRequest().equals(req)) {
				return j;
			}
		}

		return null;
	}
}
