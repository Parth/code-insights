package com.sap.codeinsights;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class GitProcessor implements Processor {
	
	private Git repo;
	private List<Coder> coders;

	public GitProcessor(Git repo, List<Coder> coders) {
		
	}
}
