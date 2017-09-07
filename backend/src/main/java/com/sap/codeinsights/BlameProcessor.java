
package com.sap.codeinsights;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.*;
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
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line = null;  
		int i = 0;
		ArrayList<BlameCoder> stagingArea = new ArrayList<BlameCoder>();
		try {
			while ((line = br.readLine()) != null) {
				try {
					System.out.println("File: "+file+" line: " +i);
					BlameCoder programmer = new BlameCoder(super.repo
						.blame()
						.setFilePath(path(file))
						.call()
						.getSourceAuthor(i)
					);

					stagingArea.add(programmer);
				} catch (Exception e) {
					//TODO update here
					return;
				}
				i++;
			}

			for (BlameCoder c : stagingArea) {
				int index = coders.indexOf(c);
				if (index < 0) coders.add(c);

				coders.get(coders.indexOf(c)).linesContributed++;
			}
		} catch (IOException e) {
		}
	}

	private void calculateEquity() {

	}

	private String path(File file) {
		return file.getAbsolutePath().replace(super.repo.getRepository().getDirectory().getParentFile().getAbsolutePath() + "/", "");
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
