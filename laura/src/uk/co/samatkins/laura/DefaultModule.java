package uk.co.samatkins.laura;

public class DefaultModule implements Module {
	
	private Laura laura;

	@Override
	public void init(Laura laura) {
		this.laura = laura;
	}

	@Override
	public boolean matches(String[] input) {
		return true;
	}

	@Override
	public void execute(String[] input) {
		laura.print("Sorry, I do not understand what you mean.");
	}

}
