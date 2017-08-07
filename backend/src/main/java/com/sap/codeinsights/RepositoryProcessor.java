package com.sap.codeinsights;
 
// TODO remove * imports
import java.io.*;
import java.util.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.FileVisitOption;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.lib.PersonIdent;

import com.sap.codeinsights.Coder;

import org.apache.commons.io.FileUtils;

public class RepositoryProcessor {
	public static Git cloneRepo(String url) {
		try { 
			File file = new File(System.getProperty("user.home") + "/code-insights/" + Math.abs((long) url.hashCode()));

			if (file.exists()) {
				Files.walk(file.toPath(), FileVisitOption.FOLLOW_LINKS)
					.sorted(Comparator.reverseOrder())
					.map(Path::toFile)
					.forEach(File::delete);
			}

			Git result = Git.cloneRepository()
					.setURI(url)
					.setDirectory(file)
					.call();
			return result;

		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//returns anyone who ever committed anything to the repository
	public static ArrayList<Coder> getCoders(Git repo) {
		ArrayList<Coder> coders = new ArrayList<Coder>();
		try {
			
			Iterable<RevCommit> commits = repo.log().all().call();
			for (RevCommit rc : commits) {
				 Coder coder = new Coder(rc.getAuthorIdent());
				if (!coders.contains(coder)) {
					coders.add(coder);
				}
			}
			return coders;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	//TODO refactor to runProcessor
	public static String process(CodeRequest r, Updatable updater) {
		updater.pushUpdate(new Update(0, "Received Request."));

		String response = "";
		ArrayList<String> people = new ArrayList<String>();

		Git repo = null;
		try {
			updater.pushUpdate(new Update(0, "Cloning Repository"));
			repo = cloneRepo(r.getURL());
			ArrayList<Coder> coders = getCoders(repo);
			
			File repoDir = repo.getRepository().getDirectory().getParentFile();

			// TODO: This will almost certainly need to be refactored in the future for more general use cases. 
			List<File> files = (List<File>) FileUtils.listFiles(repoDir, new String[] {"java"} , true);
			
			for (File file : files) {
				updater.pushUpdate(new Update(0, "Processing file: " + file.getName()));
				// TODO: This could be better, for sure, but how can we do it in a way that makes creating a processor as easy as possible? 
				DocumentationProcessor dp = new DocumentationProcessor(file, repo, coders);
			}

			r.setResult(coders);
			response += coders.toString();
			updater.pushUpdate(new Update(1, "Done. Forming result"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		repo.getRepository().close();
		return response;
	}
}
