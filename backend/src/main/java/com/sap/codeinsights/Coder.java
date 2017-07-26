package com.sap.codeinsights;

import org.eclipse.jgit.lib.PersonIdent;

public class Coder extends PersonIdent{

	//TODO Containerize this
	public int methodsContributed;

	//TODO do we use this?
	public int documentationContributed;
	public int documentedMethods;
	public int undocumentedMethods;

	public Coder(PersonIdent i) {
		super(i);
	}

	//TODO this is a bad way to do this
	public String JSON() {
		return 
			"{ " +
				"\"name\" : \"" + super.getName() + "\"," +
				"\"methodsContributed\" : \"" + methodsContributed + "\"," + 
				"\"documentedMethods\" : \"" + documentedMethods + "\"," + 
				"\"undocumentedMethods\" : \"" + undocumentedMethods + "\"" + 
			"}";
	}

	@Override
	public String toString() {
		return JSON();
	}

	@Override
	public boolean equals(final Object o) {
		final Coder c = (Coder) o;
		return 
			super.getName().equalsIgnoreCase(c.getName());
	}
}
