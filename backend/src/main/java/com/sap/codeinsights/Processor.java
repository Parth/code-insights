package com.sap.codeinsights;

import java.util.List;

public interface Processor {
	public String getType();
	public List<DocumentationCoder> getDocumentationCoders();
}
