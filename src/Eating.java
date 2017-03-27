public class Eating implements State {
	private int initTime;
	private int timeInterval;

	public Eating() {
		initTime = 0;
		timeInterval = this.randomWithRange(10, 10000);
	}

	@Override
	public Response recieveRequestFrom(Philosopher philosopher, Request packet, boolean isLeft) {

		return new Response(null);
	}

	@Override
	public void switchedTo(Philosopher philosopher) {
		System.out.println("I am eating");
	}

	@Override
	public void tick(Philosopher philosopher, int currentTime) {
		if (initTime == 0) {
			initTime = currentTime;
			return;
		}
		int timePassed = currentTime - initTime;
		if (timePassed > timeInterval){
			philosopher.setState(new Thinking());
		}
	}
}
