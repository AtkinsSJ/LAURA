package uk.co.samatkins.laura.plugins;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.samatkins.laura.Laura;
import uk.co.samatkins.laura.Module;

public class ChatModule implements Module {
	
	private Laura laura;
	private Matcher matcher;

	@Override
	public void init(Laura laura) {
		this.laura = laura;
		
		Pattern pattern = Pattern.compile("hello", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher("");
	}

	@Override
	public boolean matches(String[] input) {
		for (String word: input) {
			matcher.reset(word);
			if (matcher.find()) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void execute(String[] input) {
		Properties preferences = laura.getPreferences();
		
		String userName = preferences.getProperty("userName");
		if (userName == null) {
			laura.print("Hello, my name is LAURA. What's yours?");
			userName = laura.getInput();
			laura.print("Great to meet you, " + userName + "!");
			preferences.setProperty("userName", userName);
		} else {
			laura.print("Hello " + userName + "!");
		}
	}

}
