package com.sap.codeinsights;

import com.google.gson.JsonObject;

import org.junit.Test;
import static org.junit.Assert.*;

public class ApiTest {
	
	private static final String REPO_DEST = "https://github.com/code-insights/tests.git";

	@Test
	public void createFail() {
		String response = API.createJob("");
		assertEquals(response, "{\"error\":{\"message\":\"Null Code Request.\",\"errorNumber\":4}}");
	}

	@Test
	public void statusFail() {
		String response = API.checkJobStatus("");
		assertEquals(response, "{\"error\":{\"message\":\"Null Job.\",\"errorNumber\":4}}");
	}

	@Test
	public void resultFail() {
		String response = API.getJobResult("");
		assertEquals(response, "{\"error\":{\"message\":\"Null Job.\",\"errorNumber\":4}}");
	}

	@Test
	public void successfulCreation() throws InterruptedException {
		JsonObject json = new JsonObject();
		json.addProperty("url", REPO_DEST);
		json.addProperty("processorType", "Documentation");

		String response = API.createJob(json.toString());
		for (int i = 0; i < 5 && response.contains("error"); i++) {
			Thread.sleep(1000);
			response = API.createJob(json.toString());
		}

		assertTrue(response.contains("jobID"));
		assertTrue(response.contains("request"));
		assertTrue(response.contains("url"));
		assertTrue(response.contains("processorType"));
		assertFalse(response.contains("error"));

		String response2 = API.createJob(json.toString());
		assertTrue(response2.contains("error"));

		String status = API.checkJobStatus(response);
		
		while (!status.contains("Done.")) { 
			Thread.sleep(500);
			status = API.checkJobStatus(response);
		}

		assertTrue(status.contains("Done."));

		Thread.sleep(1000);
		String response3 = API.getJobResult(response);
		System.out.println(response3);
		assertTrue(response3.contains("parth"));
	}

	@Test
	public void badURLTest() {
	}

	@Test
	public void badProcessorTest() {
	}

	@Test
	public void successfulLogRead() {

	}

	@Test
	public void successfulResultFetch() {

	}
}
