package com.sap.codeinsights;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BlameProcessor extends Processor {
	
	public static final String TYPE = "blameprocessor";
	
	private List<BlameCoder> coders;

	public BlameProcessor(CodeRequest request, Updatable updater) {
		super(request, updater);
	}

	private void processFile(File file) {
	}

	private void calculateEquity() {

	}

	public List<BlameCoder> getCoders() {
		List<BlameCoder> blameCoders = new ArrayList<BlameCoder>();
		try {
			Iterable<RevCommit> commits = super.repo.log().all().call();
			for (RevCommit rc : commits) {
				BlameCoder blameCoder = new BlameCoder(rc.getAuthorIdent());
				if (!blameCoders.contains(blameCoder)) {
					blameCoders.add(blameCoder);
				}
				blameCoders.get(blameCoders.indexOf(blameCoder)).commitsContributed++;
			}
			return blameCoders;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void getResult(Resultable result) {
		updater.pushUpdate(new Update(0, "Started Request"));

		
		try {
			updater.pushUpdate(new Update(0, "Cloning Repository"));
			super.cloneRepo();

			updater.pushUpdate(new Update(0, "Getting Coders"));
			this.coders = getCoders();

			Files.walk(Paths.get(super.workingDirectory.toURI()))
				.filter(Files::isRegularFile)
				.forEach((file) -> {
					processFile(file.toFile());
				});

			calculateEquity();


			updater.pushUpdate(new Update(0, "Forming result"));
			result.setResult(coders);
			updater.pushUpdate(new Update(1, "Done."));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public String getType() {
		return "GitProcessor";
	}
}
