package com.sap.codeinsights;

import java.util.Map;

public class API {
	public static String processessRepository(String url) {
		 return RepositoryProcessor.process(url);
	}

	public static String processorsAvailable() {
		return ProcessorService.allProcessors();
	}

	//public static String getAllRecipes(MongoDatabase database) {
	//	return RecipeService.getAllRecipes(database).toString();
	//}

	//public static String submitRecipe(MongoDatabase database, String json, StatusCode code) {
	//	Gson gson = new Gson();
	//	Recipe recipe = gson.fromJson(json, Recipe.class);
	//	return RecipeService.submitRecipe(recipe, database).toString();
	//}
}
