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

public class DocumentationProcessor extends VoidVisitorAdapter implements Processor {
	private File file;
	private List<DocumentationCoder> documentationCoders;
	private Git repo;

	public DocumentationProcessor(File file, Git repo, List<DocumentationCoder> documentationCoders) throws FileNotFoundException, ParseException, IOException {
		super();
		this.file = file;
		this.repo = repo;
		this.documentationCoders = documentationCoders;

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
	public void visit(MethodDeclaration n, Object args) {
		if (n.getComment().isPresent()) {
			hasComments(n);
		} else {
			noComments(n);
		}
	}

	@Override
	public String getType() {
		return "DocumentationProcessor";
	}

	@Override
	public String toString() {
		return this.getType();
	}

	public List<DocumentationCoder> getDocumentationCoders() {
		return documentationCoders;
	}
}
