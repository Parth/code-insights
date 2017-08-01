package com.sap.codeinsights;

import java.util.Map;

public class API {
	public static String processessRepository(String url) {
		 return RepositoryProcessor.process(url);
	}

	public static String processorsAvailable() {
		return ProcessorService.allProcessors();
	}
}
