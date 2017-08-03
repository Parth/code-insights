package com.sap.codeinsights;

import static spark.Spark.*;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;

import java.util.Map;

public class Server {

	public static boolean setupServer(){ 
		File file = new File(System.getProperty("user.home") + "/code-insights-working-directory");
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
			System.err.println("Server could not be initialized, shutting down now.");
			System.exit(1);
		}

		post("/repository-processor", (req, res) -> {
			res.header("Access-Control-Allow-Origin", "*");
			String ret = API.processessRepository(req.body());
			return ret;
		});
	}
}
