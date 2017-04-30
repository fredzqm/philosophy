package philosopher;

public class Sleep extends PState {
	private Philosopher philosopher;

	public Sleep(Philosopher philosopher) {
		this.philosopher = philosopher;
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
}
