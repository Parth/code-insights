package com.sap.codeinsights;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GitProcessor implements Processor {
	
	private Git repo;
	private List<GitCoder> coders;

	public GitProcessor(Git repo, List<GitCoder> coders) {
		this.repo = repo; 
		this.coders = coders;

		ArrayList<GitCoder> gitCoders = new ArrayList<GitCoder>();

		try {
			Iterable<RevCommit> commits = repo.log().all().call();
			for (RevCommit rc : commits) {
				GitCoder gitCoder = new GitCoder(rc.getAuthorIdent());
				if (!gitCoders.contains(gitCoder)) {
					gitCoders.add(gitCoder);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getType() {
		return "GitProcessor";
	}
}
