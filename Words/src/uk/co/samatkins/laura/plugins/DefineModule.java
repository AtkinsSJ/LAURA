package uk.co.samatkins.laura.plugins;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.samatkins.laura.Laura;
import uk.co.samatkins.laura.Module;

import com.wordnik.client.api.WordApi;
import com.wordnik.client.common.ApiException;
import com.wordnik.client.model.Definition;

public class DefineModule implements Module {

	private Laura laura;
	private Matcher matcher;
	private String API_KEY = "612cdc57340323631f3010ea81408e2ed10a5c239cc6116df";
	
	@Override
	public void init(Laura laura) {
		this.laura = laura;
		Pattern pattern = Pattern.compile("define", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher("");
	}

	@Override
	public boolean matches(String[] input) {
		matcher.reset(input[0]);
		return matcher.find();
	}

	@Override
	public void execute(String[] input) {
		if (input.length < 2) {
			laura.print("You need to give me a word to define.");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i=1; i<input.length; i++) {
			sb.append(input[i]);
		}
		String query = sb.toString();
		
		WordApi api = new WordApi();
		api.getInvoker().addDefaultHeader("api_key", API_KEY);
		try {
			List<Definition> definitions = api.getDefinitions(
					query,
					null, //partOfSpeech, 
					null, //sourceDictionaries, 
					3, //limit, 
					"false", //includeRelated, 
					"false", //useCanonical, 
					"false" //includeTags
			);
			
			for (Definition def: definitions) {
				laura.print(query + ": (" + def.getPartOfSpeech() + ")");
				laura.print(def.getText());
			}
		} catch (ApiException e) {
			laura.print("Sorry, but I could not contact the definition server.");
			e.printStackTrace();
		}
		
	}

}
