package com.sap.codeinsights;

import java.util.Hashtable;
import java.util.List;

import com.google.gson.JsonArray;

public class ProcessorService {
	public static final Hashtable<Job, Status> jobs = new Hashtable<Job, Status>();
	public static final Hashtable<Job, Object> results = new Hashtable<Job, Object>();

	public static JsonArray allProcessors() {
		JsonArray processors = new JsonArray();
		processors.add(DocumentationProcessor.TYPE);
		return processors;
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

	public static Object getResult(Job job) throws Error {
		if (!jobs.containsKey(job)) {
			throw new Error("Job does not exist.", Error.JOB_NOT_FOUND);
		}

		if (jobs.get(job).getStatusCode() == 1) {
			Object ret = results.get(job);
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
			startJobForResult(newJob);
		};

		Thread t = new Thread(task);
		t.start();
		return t.isAlive();
	}

	private static void startJobForResult(Job job) {
		Updatable simpleUpdate = (update) -> {
			jobs.get(job).pushUpdate(update);
		};

		Resultable simpleResult = (result) -> {
			results.put(job, result);
		};

		CodeRequest r = job.getCodeRequest();

		switch(r.getProcessorType().toLowerCase()) {
			case DocumentationProcessor.TYPE:
				new DocumentationProcessor(job.getCodeRequest(), simpleUpdate).getResult(simpleResult);
				break;

			//case BlameProcessor.getType().toLowercase():
			//	return new BlameProcessor(job.getCodeRequest(), simpleUpdate).getResult(simpleResult);
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
