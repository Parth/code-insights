package com.sap.codeinsights;

import java.util.List;
import org.eclipse.jgit.api.Git;

public class GitProcessor implements Processor {
	
	private Git repo;
	private List<DocumentationCoder> coders;

	public GitProcessor(Git repo, List<GitCoder> coders) {
		
	}

	@Override
	public String getType() {
		return "GitProcessor";
	}
}
