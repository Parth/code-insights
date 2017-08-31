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





			for (File file : files) {
				updater.pushUpdate(new Update(0, "Processing file: " + file.getName()));
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
