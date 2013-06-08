package uk.co.samatkins.laura;

public interface Module {
	
	public void init(Laura laura);
	public boolean matches(String[] input);
	public void execute(String[] input);
	
}
