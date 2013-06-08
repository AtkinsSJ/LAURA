package uk.co.samatkins.laura.plugins;

import uk.co.samatkins.laura.Module;

public class TestModule implements Module {

	@Override
	public void init() {
		System.out.println("Initialising Test Module");
	}

	@Override
	public boolean matches(String[] input) {
		System.out.println("Test Module matches all input!");
		return true;
	}

	@Override
	public void execute(String[] input) {
		System.out.println("Executing Test Module on input: " + input);
	}

}
