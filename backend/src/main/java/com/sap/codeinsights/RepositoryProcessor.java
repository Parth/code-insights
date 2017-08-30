package com.sap.codeinsights;

import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;

import java.io.File;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RepositoryProcessor {
	public synchronized static Git cloneRepo(String url) {
		try {
			File file = new File(Server.WORKING_DIR + Math.abs((long) url.hashCode()));

			if (file.exists()) {
				Files.walk(file.toPath(), FileVisitOption.FOLLOW_LINKS)
						.sorted(Comparator.reverseOrder())
						.map(Path::toFile)
						.forEach(File::delete);
			}


			SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
				@Override
				protected void configure(OpenSshConfig.Host host, Session session) {
					session.setConfig("StrictHostKeyChecking", "no");
					session.setUserInfo(new UserInfo() {
						@Override
						public String getPassphrase() { return null; }

						@Override
						public String getPassword() { return null; }

						@Override
						public boolean promptPassword(String message) { return false; }

						@Override
						public boolean promptPassphrase(String message) { return true; }

						@Override
						public boolean promptYesNo(String message) { return false; }

						@Override
						public void showMessage(String message) { }
					});
				}
			};

			Git result = null;
			if (StringUtils.containsIgnoreCase("https", url)) {
				result = Git.cloneRepository()
					.setURI(url)
					.setDirectory(file)
					.setTransportConfigCallback(transport -> {
						SshTransport sshTransport = (SshTransport) transport;
						sshTransport.setSshSessionFactory(sshSessionFactory);
					})
					.call();
			} else {
				result = Git.cloneRepository(j
					.setURI(url)
					.setDirectory(file)
					.call();
			}

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//returns anyone who ever committed anything to the repository
	public synchronized static ArrayList<DocumentationCoder> getCoders(Git repo) {
		ArrayList<DocumentationCoder> documentationCoders = new ArrayList<DocumentationCoder>();
		try {

			Iterable<RevCommit> commits = repo.log().all().call();
			for (RevCommit rc : commits) {
				DocumentationCoder documentationCoder = new DocumentationCoder(rc.getAuthorIdent());
				if (!documentationCoders.contains(documentationCoder)) {
					documentationCoders.add(documentationCoder);
				}
			}
			return documentationCoders;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	//TODO refactor to runProcessor
	public static String process(CodeRequest r, Updatable updater, Resultable result) {
		updater.pushUpdate(new Update(0, "Received Request."));

		String response = "";
		ArrayList<String> people = new ArrayList<String>();

		Git repo = null;
		try {
			updater.pushUpdate(new Update(0, "Cloning Repository"));
			repo = cloneRepo(r.getURL());

			ArrayList<DocumentationCoder> documentationCoders = getCoders(repo);

			File repoDir = repo.getRepository().getDirectory().getParentFile();

			// TODO: This will almost certainly need to be refactored in the future for more general use cases.
			List<File> files = (List<File>) FileUtils.listFiles(repoDir, new String[]{"java"}, true);

			for (File file : files) {
				updater.pushUpdate(new Update(0, "Processing file: " + file.getName()));
				// TODO: This could be better, for sure, but how can we do it in a way that makes creating a processor as easy as possible?
				DocumentationProcessor dp = new DocumentationProcessor(file, repo, documentationCoders);
			}

			updater.pushUpdate(new Update(0, "Forming result"));
			result.setResult(documentationCoders);
			response += documentationCoders.toString();
			updater.pushUpdate(new Update(1, "Done."));

		} catch (Exception e) {
			e.printStackTrace();
		}
		repo.getRepository().close();
		return response;
	}
}
