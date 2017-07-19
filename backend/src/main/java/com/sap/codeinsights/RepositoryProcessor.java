package com.sap.codeinsights;
 
import java.io.*;
import java.util.*;

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

	//TODO if file exists it don't clone it obvi
	private static Git cloneRepo(String url) {
		try { 
			File file = new File(System.getProperty("user.home") + "/code-insights/" + Math.abs((long) url.hashCode()));

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

	//returns anyone who ever committed anything to the repoistory
	private static ArrayList<Coder> getCoders(Git repo) {
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


	public static String process(String url) {

		String response = "";
		ArrayList<String> people = new ArrayList<String>();

		Git repo = null;
		try {
			repo = cloneRepo(url);
			ArrayList<Coder> coders = getCoders(repo);
			
			File repoDir = repo.getRepository().getDirectory().getParentFile();

			List<File> files = (List<File>) FileUtils.listFiles(repoDir, new String[] {"java"} , true);
			
			int i = 0;
			for (File file : files) {
				i++;
				System.out.println((((double)i/files.size())*100) + "%");
				DocumentationProcessor dp = new DocumentationProcessor(file, repo, coders);
			}

			response += coders.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		repo.getRepository().close();
		return response;
	}
}
