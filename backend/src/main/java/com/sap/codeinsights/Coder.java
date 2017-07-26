package com.sap.codeinsights;

import org.eclipse.jgit.lib.PersonIdent;

public class Coder extends  PersonIdent{

	public int methodsContributed;

	public int documentationContributed;
	public int documentedMethods;
	public int undocumentedMethods;

	public Coder(PersonIdent i) {
		super(i);
	}

	public String JSON() {
		return 
			"{ " +
				"\"name\" : \"" + super.getName() + "\"," +
				"\"email\" : \"" + super.getEmailAddress() + "\"," +
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
			super.getName().equals(c.getName()) ||
			super.getEmailAddress().equals(c.getEmailAddress());
	}
}
