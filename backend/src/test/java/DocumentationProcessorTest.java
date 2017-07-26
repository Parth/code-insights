package com.sap.codeinsights;

import org.junit.Test;
import static org.junit.Assert.*;

import org.eclipse.jgit.api.Git;

import java.io.File;
import java.util.List;

public class DocumentationProcessorTest {

	private static final String REPO_DEST = "https://github.com/code-insights/tests.git";

    @Test
	public void testContributorCount() {
		Git testRepo = RepositoryProcessor.cloneRepo(REPO_DEST);
        assertTrue("Should only be 3 contributors", RepositoryProcessor.getCoders(testRepo).size() == 3);
    }

	@Test
	public void testFileWithNoComments() throws Exception {
		Git testRepo = RepositoryProcessor.cloneRepo(REPO_DEST);
		List<Coder> coders = RepositoryProcessor.getCoders(testRepo);

		File repoDir = testRepo.getRepository().getDirectory().getParentFile();
		File testFile = new File(repoDir.getPath() + "/DocumentationProcessorTests/Test1.java");

		DocumentationProcessor noComments = new DocumentationProcessor(testFile, testRepo, coders);
		assertNotNull(coders);

		for (Coder c : coders) {
			assertEquals(c.methodsContributed, 1);
			assertEquals(c.documentedMethods, 0);
			assertEquals(c.undocumentedMethods, 1);
		}
	}

	@Test
	public void testFileWithComments() throws Exception {
		Git testRepo = RepositoryProcessor.cloneRepo(REPO_DEST);
		List<Coder> coders = RepositoryProcessor.getCoders(testRepo);

		File repoDir = testRepo.getRepository().getDirectory().getParentFile();
		File testFile = new File(repoDir.getPath() + "/DocumentationProcessorTests/Test2.java");

		DocumentationProcessor noComments = new DocumentationProcessor(testFile, testRepo, coders);
		assertNotNull(coders);

		for (Coder c : coders) {
			assertEquals(c.methodsContributed, 1);
			assertEquals(c.documentedMethods, 1);
			assertEquals(c.undocumentedMethods, 0);
		}
	}
}
