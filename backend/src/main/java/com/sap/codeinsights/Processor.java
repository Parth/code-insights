package com.sap.codeinsights;

public class Processor {
	
	private Git repo;
	private CodeRequest request;
	private File file; 

	private Updatable updater; 
	private CodeRequest request;

	public Processor(CodeRequest request, Updateable updater) {
		this.request = request;
		this.updater = updater;
	}

	public void cloneRepo() {
		String url = r.getURL();
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
				repo = Git.cloneRepository(j
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
