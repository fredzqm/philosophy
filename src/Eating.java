public class Eating implements State {
	private double initTime;
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
		philosopher.getChopstick(true).dirty();
		philosopher.getChopstick(false).dirty();
	}

	@Override
	public void tick(Philosopher philosopher, double currentTime) {
		if (initTime == 0) {
			initTime = currentTime;
			return;
		}
		double timePassed = currentTime - initTime;
		if (timePassed > timeInterval){
			philosopher.setState(new Thinking());
		}
	}
}
