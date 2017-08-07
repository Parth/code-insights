package com.sap.codeinsights;

import static spark.Spark.*;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;

import java.util.Map;

public class Server {
	public static boolean setupServer(){ 
		File file = new File(System.getProperty("user.home") + "/code-insights");
		try {
			if (!file.exists()) {
				Files.createDirectory(file.toPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static void main(String args[]) {
		if (!setupServer()) {
			System.err.println("Server could not be initialized, it was a good try, shutting down now.");
			System.exit(1);
		}

		post("/create-job", (req, res) -> {
			res.header("Access-Control-Allow-Origin", "*");
			return API.createJob(req.body());
		});

		post("/check-job", (req, res) -> {
			res.header("Access-Control-Allow-Origin", "*");
			return API.checkJobStatus(req.body());
		});

		post("/job-log", (req, res) -> {
			res.header("Access-Control-Allow-Origin", "*");
			return API.getJobLog(req.body());
		});

		post("/job-result", (req, res) -> {
			res.header("Access-Control-Allow-Origin", "*");
			return API.getJobResult(req.body());
		});
	}
}
