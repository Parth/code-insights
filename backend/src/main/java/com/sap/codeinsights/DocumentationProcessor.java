package com.sap.codeinsights;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.comments.Comment;

public class DocumentationProcessor extends Processor {

	private List<DocumentationCoders> coders;
	private static final String[] FILE_FILTER = new String[]{"java"};

	public DocumentationProcessor(CodeRequest request, Updateable updater) {
		super(request, updater);
	}

	public List<DocumentationCoder> getCoders(Git repo) {
		List<DocumentationCoder> documentationCoders = new ArrayList<DocumentationCoder>();
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

	private void processFile(File file) {
		updater.pushUpdate(new Update(0, "Processing file: " + file.getName()));
		new VoidVisitorAdapter <Object> () {
			@Override
			public void visit(MethodDeclaration n, Object args) {
				if (n.getComment().isPresent()) {
					hasComments(n);
				} else {
					noComments(n);
				}
			}
		}.visit(JavaParser.parse(file));
	}

	@Override
	public void getResult(Resultable result) {
		updater.pushUpdate(new Update(0, "Started Request");

		try {
			updater.pushUpdate(new Update(0, "Cloning Repository"));
			this.repo = cloneRepo(request.getURL());
			this.documentationCoders = getCoders(repo);

			File repoDir = repo.getRepository().getDirectory().getParentFile();
			List<File> files = (List<File>) FileUtils.listFiles(repoDir, FILE_FILTER, true);

			for (File file : files) {
				processFile(file);
			}

			updater.pushUpdate(new Update(0, "Forming result"));
			result.setResult(documentationCoders);
			updater.pushUpdate(new Update(1, "Done."));

		} catch (Exception e) {
			// TODO handle on your own.
			e.printStackTrace();
		}

		FileInputStream inputStream = new FileInputStream(file);
		this.visit(JavaParser.parse(inputStream), null);
		inputStream.close();
	}

	private void hasComments(MethodDeclaration n) {
		Comment comment = n.getComment().get();
		ArrayList<DocumentationCoder> commenters = new ArrayList<DocumentationCoder>();
		ArrayList<DocumentationCoder> programmers = new ArrayList<DocumentationCoder>();
		ArrayList<DocumentationCoder> allContributors = new ArrayList<DocumentationCoder>();

		try { 
			for (int i = comment.getBegin().get().line; i <= comment.getEnd().get().line; i++) {
				DocumentationCoder commenter = new DocumentationCoder(repo.blame().setFilePath(path()).call().getSourceAuthor(i-1));
				if (!commenters.contains(commenter)) {
					commenters.add(commenter);
				}
			}

			for (int i = n.getBegin().get().line; i <= n.getEnd().get().line; i++) {
				DocumentationCoder programmer = new DocumentationCoder(repo.blame().setFilePath(path()).call().getSourceAuthor(i-1));
				if (!programmers.contains(programmer)) {
					programmers.add(programmer);
				}
			}
		} catch  (GitAPIException e) {
			e.printStackTrace();
		}

		allContributors.addAll(programmers);
		for (DocumentationCoder c : commenters) {
			if (!allContributors.contains(c)) {
				allContributors.add(c);
			}
		}

		for (DocumentationCoder contributor : allContributors) {
			documentationCoders.get(documentationCoders.indexOf(contributor)).methodsContributed++;
		}
		
		for (DocumentationCoder commenter : commenters) {
			documentationCoders.get(documentationCoders.indexOf(commenter)).documentationContributed++;
		}

		for (DocumentationCoder programmer : programmers) {
			documentationCoders.get(documentationCoders.indexOf(programmer)).documentedMethods++;
		}
	}

	// TODO refactor to hashset of some sort
	private void noComments(MethodDeclaration n) {
		ArrayList<DocumentationCoder> programmers = new ArrayList<DocumentationCoder>();

		try {
			for (int i = n.getBegin().get().line; i <= n.getEnd().get().line; i++) {
				DocumentationCoder programmer = new DocumentationCoder(repo.blame().setFilePath(path()).call().getSourceAuthor(i-1));
				if (!programmers.contains(programmer)) {
					programmers.add(programmer);
				}
			}
		} catch  (GitAPIException e) {
			e.printStackTrace();
		}

		for (DocumentationCoder programmer: programmers) {
			documentationCoders.get(documentationCoders.indexOf(programmer)).methodsContributed++;
			documentationCoders.get(documentationCoders.indexOf(programmer)).undocumentedMethods++;
		}
	}

	private String path() {
		return file.getAbsolutePath().replace(repo.getRepository().getDirectory().getParentFile().getAbsolutePath()+"/", "");
	}
	

	@Override
	public static String getType() {
		return "DocumentationProcessor";
	}

	@Override
	public String toString() {
		return this.getType();
	}

}
