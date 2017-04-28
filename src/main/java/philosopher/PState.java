package philosopher;

public class PState {
	protected boolean active;

	public void onStart() {
		active = true;
	}
	

	public void onExit() {
		active = false;
	}

}
