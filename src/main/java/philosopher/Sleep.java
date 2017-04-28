package philosopher;

public class Sleep extends PState {
	private Philosopher philosopher;

	public Sleep(Philosopher philosopher) {
		this.philosopher = philosopher;
	}
	
	@Override
	public void onStart() {
		this.philosopher.getMyself().finishEating();
		this.philosopher.dropTheBottle();
		super.onStart();
	}
	
}
