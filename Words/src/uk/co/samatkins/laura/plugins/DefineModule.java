package uk.co.samatkins.laura.plugins;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

import uk.co.samatkins.laura.Laura;
import uk.co.samatkins.laura.Module;

public class DefineModule implements Module {

	private Laura laura;
	private Matcher matcher;
	private String API_KEY = "612cdc57340323631f3010ea81408e2ed10a5c239cc6116df";
	
	private ObjectMapper mapper;
	
	@Override
	public void init(Laura laura) {
		this.laura = laura;
		Pattern pattern = Pattern.compile("define", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher("");
		
		mapper = new ObjectMapper();
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
		
		// Ask wordnik for the definition
		try {
			URL wordnikUrl = new URL("http://api.wordnik.com/v4/word.json/"
					+ query + "/definitions?api_key=" + API_KEY + "&limit=1");
			HttpURLConnection conn = (HttpURLConnection) wordnikUrl.openConnection();
			conn.connect();
			
			String jsonString = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
			
			Map<String,Object> json = mapper.readValue(jsonString, Map.class);
			
			laura.print(json.toString());
			
		} catch (MalformedURLException e) {
			laura.print("Sorry, I could not reach the wordnik server to get the definition.");
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
