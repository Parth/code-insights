package com.sap.codeinsights;

import org.junit.Test;
import static org.junit.Assert.*;

public class StatusManagementTest {
	
	@Test
	public void StatusCreationTest() {
		Status s = new Status("Current Status");

		assertTrue(s.getCurrentStatus().equals("Current Status"));
		assertTrue(s.getStatusLog().equals("Current Status"));
		assertEquals(s.getStatusCode(), 0);
	}

	@Test
	public void StatusPushTests() {
		Status s = new Status("A");
		s.setCurrentStatus("B");

		assertTrue(s.getCurrentStatus().equals("B"));
		assertTrue(s.getStatusLog().equals("A\nB"));
		assertEquals(s.getStatusCode(), 0);

		s.setStatusCode(2);
		assertEquals(s.getStatusCode(), 2);

		s.pushUpdate(new Update(1, "C"));
		assertTrue(s.getCurrentStatus().equals("C"));
		assertEquals(s.getStatusCode(), 1);
		assertTrue(s.getStatusLog().equals("A\nB\nC"));
	}
}
