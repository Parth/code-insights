package com.sap.codeinsights;

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
		System.out.println(response);
		assertEquals(response, "{\"error\":{\"message\":\"Null Job.\",\"errorNumber\":4}}");
	}

	@Test
	public void logFail() {
		String response = API.getJobLog("");
		assertEquals(response, "{\"error\":{\"message\":\"Null Job.\",\"errorNumber\":4}}");
	}

	@Test
	public void resultFail() {
		String response = API.getJobResult("");
		assertEquals(response, "{\"error\":{\"message\":\"Null Job.\",\"errorNumber\":4}}");
	}

	@Test
	public void successfulCreation() {
		
	}

	@Test
	public void successfulStatusCheck() {

	}

	@Test
	public void successfulLogRead() {

	}

	@Test
	public void successfulResultFetch() {

	}
}
