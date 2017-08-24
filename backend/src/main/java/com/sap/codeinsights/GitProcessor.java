package com.sap.codeinsights;

import org.eclipse.jgit.api.Git;

public class GitProcessor implements Processor {
	
	private Git repo;
	private List<DocumentationCoder> coders;

	public GitProcessor(Git repo, List<DocumentationCoder> coders) {
		
	}
}
