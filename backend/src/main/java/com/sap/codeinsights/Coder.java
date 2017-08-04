package com.sap.codeinsights;

import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

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

    public JsonObject toJson() {
        JsonParser parser = new JsonParser();
        return parser.parse(this.toString()).getAsJsonObject();
    }

	@Override
	public boolean equals(final Object o) {
		final Coder c = (Coder) o;
		return 
			super.getName().equalsIgnoreCase(c.getName());
	}

	@Override
	public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
