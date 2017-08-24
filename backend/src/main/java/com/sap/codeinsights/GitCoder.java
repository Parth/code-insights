package com.sap.codeinsights;

import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.eclipse.jgit.lib.PersonIdent;

public class GitCoder extends PersonIdent {
	public int commitsContributed;
	public int filesChanged; 
	public int linesAdded; 
	public int linesDeleted;

	public GitCoder(PersonIdent i) {
		super(i);
	}

    public JsonObject toJson() {
        JsonParser parser = new JsonParser();
        return parser.parse(this.toString()).getAsJsonObject();
    }

	@Override
	public boolean equals(final Object o) {
		final PersonIdent c = (PersonIdent) o;
		return 
			super.getName().equalsIgnoreCase(c.getName());
	}
}
