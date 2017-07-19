package com.sap.codeinsights;

import org.junit.Test;
import static org.junit.Assert.*;

import org.eclipse.jgit.api.Git;

public class DocumentationProcessorTest {

	private static final String REPO_DEST = "https://github.com/code-insights/tests.git";

    @Test
	public void testFileWithNoComments() {
		Git testRepo = RepositoryProcessor.cloneRepo(REPO_DEST);
	
		System.out.println(RepositoryProcessor.getCoders(testRepo));

        assertTrue("Should only be 3 contributors", RepositoryProcessor.getCoders(testRepo).size() == 3);
    }
}
