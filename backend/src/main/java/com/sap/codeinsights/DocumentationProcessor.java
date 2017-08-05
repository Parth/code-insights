package com.sap.codeinsights;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.comments.Comment;

public class DocumentationProcessor extends VoidVisitorAdapter implements Processor {
	private File file;
	private List<Coder> coders;
	private Git repo;

	public DocumentationProcessor(File file, Git repo, List<Coder> coders) throws FileNotFoundException, ParseException, IOException {
		super();
		this.file = file;
		this.repo = repo;
		this.coders = coders;

		FileInputStream inputStream = new FileInputStream(file);
		this.visit(JavaParser.parse(inputStream), null);
		inputStream.close();
	}

	private void hasComments(MethodDeclaration n) {
		Comment comment = n.getComment().get();
		ArrayList<Coder> commenters = new ArrayList<Coder>();
		ArrayList<Coder> programmers = new ArrayList<Coder>();
		ArrayList<Coder> allContributors = new ArrayList<Coder>();

		try { 
			for (int i = comment.getBegin().get().line; i <= comment.getEnd().get().line; i++) {
				Coder commenter = new Coder(repo.blame().setFilePath(path()).call().getSourceAuthor(i-1));
				if (!commenters.contains(commenter)) {
					commenters.add(commenter);
				}
			}

			for (int i = n.getBegin().get().line; i <= n.getEnd().get().line; i++) {
				Coder programmer = new Coder(repo.blame().setFilePath(path()).call().getSourceAuthor(i-1));
				if (!programmers.contains(programmer)) {
					programmers.add(programmer);
				}
			}
		} catch  (GitAPIException e) {
			e.printStackTrace();
		}

		allContributors.addAll(programmers);
		for (Coder c : commenters) {
			if (!allContributors.contains(c)) {
				allContributors.add(c);
			}
		}

		for (Coder contributor : allContributors) {
			coders.get(coders.indexOf(contributor)).methodsContributed++;
		}
		
		for (Coder commenter : commenters) {
			coders.get(coders.indexOf(commenter)).documentationContributed++;
		}

		for (Coder programmer : programmers) {
			coders.get(coders.indexOf(programmer)).documentedMethods++;
		}
	}

	private void noComments(MethodDeclaration n) {
		ArrayList<Coder> programmers = new ArrayList<Coder>();

		try {
			for (int i = n.getBegin().get().line; i <= n.getEnd().get().line; i++) {
				Coder programmer = new Coder(repo.blame().setFilePath(path()).call().getSourceAuthor(i-1));
				if (!programmers.contains(programmer)) {
					programmers.add(programmer);
				}
			}
		} catch  (GitAPIException e) {
			e.printStackTrace();
		}

		for (Coder programmer: programmers) {
			coders.get(coders.indexOf(programmer)).methodsContributed++;
			coders.get(coders.indexOf(programmer)).undocumentedMethods++;
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

	public List<Coder> getCoders() {
		return coders;
	}
}
