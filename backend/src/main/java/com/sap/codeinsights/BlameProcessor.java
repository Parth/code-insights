package com.sap.codeinsights;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BlameProcessor implements Processor {
	
	private Git repo;
	private List<GitCoder> coders;

	public BlameProcessor(Git repo, List<GitCoder> coders) {
		this.repo = repo; 
		this.coders = coders;
		Files.walk(Paths.get(path))
			.filter(Files::isRegularFile)
			.forEach((file) -> {
				processFile(file);
			});
	}

	private void processFile(File file) {
		System.out.println(file);
	}

	@Override
	public String getType() {
		return "GitProcessor";
	}
}
