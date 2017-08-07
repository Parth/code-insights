package com.sap.codeinsights;

import org.junit.Test;
import static org.junit.Assert.*;

public class ProcessorServiceTest {
	
	private static final String REPO_DEST = "https://github.com/code-insights/tests.git";

	@Test(expected = Exception.class)
	public void testNoJobCheck() {
		Job j = new Job(System.currentTimeMillis(), new CodeRequest(REPO_DEST, "Documentation")
		ProcessorService.checkJobStatus(j);
	}
}
