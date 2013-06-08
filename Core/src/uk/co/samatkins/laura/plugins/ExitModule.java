package uk.co.samatkins.laura.plugins;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.samatkins.laura.Laura;
import uk.co.samatkins.laura.Module;

public class ExitModule implements Module {
	
	private Laura laura;
	private Matcher matcher;

	@Override
	public void init(Laura laura) {
		this.laura = laura;
		Pattern pattern = Pattern.compile("exit|quit|bye|goodbye", Pattern.CASE_INSENSITIVE);
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
		laura.exit();
	}

}
