package uk.co.samatkins.laura.plugins;

import uk.co.samatkins.laura.Laura;
import uk.co.samatkins.laura.Module;

public class TestModule implements Module {
	
	private Laura laura;

	@Override
	public void init(Laura laura) {
		this.laura = laura;
		
		laura.print("Initialising Test Module");
	}

	@Override
	public boolean matches(String[] input) {
		for (String word: input) {
			if (word.equals("test")) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void execute(String[] input) {
		laura.print("Executing Test Module on input: " + input);
	}

}
