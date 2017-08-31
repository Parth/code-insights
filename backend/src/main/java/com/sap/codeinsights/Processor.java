package com.sap.codeinsights;

import com.jcraft.jsch.Session;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;

import java.io.File;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public abstract class Processor {

	protected Git repo;
	protected File file;

	protected Updatable updater;
	protected CodeRequest request;

	public Processor(CodeRequest request, Updatable updater) {
		this.request = request;
		this.updater = updater;
	}

	public static abstract String getType();

	public void cloneRepo() {
		String url = request.getURL();
		try {
			this.file = new File(
				Server.WORKING_DIR
				+ "/" + this.getType() +"/"
				+ Math.abs((long) url.hashCode()));

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
				}
			};

			if (StringUtils.containsIgnoreCase("https", url)) {
				repo = Git.cloneRepository()
					.setURI(url)
					.setDirectory(file)
					.setTransportConfigCallback(transport -> {
						SshTransport sshTransport = (SshTransport) transport;
						sshTransport.setSshSessionFactory(sshSessionFactory);
					})
					.call();
			} else {
				repo = Git.cloneRepository()
					.setURI(url)
					.setDirectory(file)
					.call();
			}
		} catch (Exception e) {
			// TODO handle better
			e.printStackTrace();
			repo = null;
		}
	}

	public static String getType() {
		return "GeneralProcessor";
	}

	public abstract void getResult(Resultable result);
}
