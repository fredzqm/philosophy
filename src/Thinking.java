public class Thinking extends State {
	private double initTime;
	private int timeInterval;

	@Override
	public Response recieveRequestFrom(Philosopher philosopher, Request packet, boolean isLeft) {
		Chopstick chopstick = philosopher.getChopstick(isLeft);
		philosopher.setChopstick(null, isLeft);
		return new Response(chopstick);
	}

	@Override
	public void switchedTo(Philosopher philosopher) {
		System.out.println("I am thinking");
		if (philosopher.isLeftFirst()) {
			philosopher.getChopstick(false).clean();
			philosopher.getChopstick(true).clean();
		} else {
			philosopher.getChopstick(true).clean();
			philosopher.getChopstick(false).clean();
		}
	}

	@Override
	protected void timeOut(Philosopher philosopher) {
		philosopher.setState(new Hungry());
	}

}
