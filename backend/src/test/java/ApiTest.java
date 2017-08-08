package com.sap.codeinsights;

import org.junit.Test;
import static org.junit.Assert.*;

public class ApiTest {
	
	private static final String REPO_DEST = "https://github.com/code-insights/tests.git";

	@Test
	public void StatusTestFail() {
		System.out.println(API.createJob(""));
	}
}
