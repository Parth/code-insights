package com.sap.codeinsights;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class ProcessorServiceTest {
	
	private static final String REPO_DEST = "https://github.com/code-insights/tests.git";

	@Test(expected=Error.class)
	public void testNoJobCheck() throws Exception {
		Job j = new Job(System.currentTimeMillis(), new CodeRequest(REPO_DEST, "Documentation"));
		ProcessorService.getStatus(j);
	}

	@Test
	public void createNewJob() throws InterruptedException, Error { 
		CodeRequest cr = new CodeRequest(REPO_DEST, "Documentation");

		Job j = null;

		for (int i = 0; (j == null) && i < 5; i++) { 
			try {
				j = ProcessorService.createJob(cr);
			} catch (Error e) {
				Thread.sleep(1000);
			}
		}

		Status s = null;

		assertEquals(j.getCodeRequest(), cr);

		for (int i = 0; (s == null) && i < 5; i++) { 
			try {
				s = ProcessorService.getStatus(j);
			} catch (Error e) {
				Thread.sleep(1000);
			}
		}

		assertNotNull(s);

		assertTrue(s.getCurrentStatus() != null);
		assertTrue(!s.getCurrentStatus().isEmpty());
		assertTrue(s.getStatusLog() != null);
		assertTrue(!s.getStatusLog().isEmpty());

		for (int i = 0; (s.getStatusCode() != 1) && i < 5; i++) { 
			Thread.sleep(1000);
		}

		assertEquals(s.getStatusCode(), 1);
		assertTrue(s.getStatusLog().contains("Test1.java"));
		assertTrue(s.getStatusLog().contains("Test2.java"));
		assertTrue(s.getCurrentStatus().equals("Done."));
		List<Coder> coders = ProcessorService.getResult(j);
		System.out.println(coders);
		assertNotNull(coders);
		assertTrue(coders.size() > 2);
	}

}

