public class Hungry implements State {
	private double initTime;
	private int timeInterval;

	public Hungry() {
		initTime = 0;
		timeInterval = this.randomWithRange(10, 10000);
	}

	@Override
	public Response recieveRequestFrom(Philosopher philosopher, Request packet, boolean isLeft) {
		Chopstick chop = philosopher.getChopstick(isLeft);
		if (chop != null && chop.isDirty()) {
			philosopher.setChopstick(null, isLeft);
			return new Response(chop);
		} else {
			return new Response(null);
		}
	}

	@Override
	public void switchedTo(Philosopher philosopher) {
		System.out.println("I am hugry");
		if (philosopher.isLeftFirst()) {
			requestChopstick(philosopher, true);
			requestChopstick(philosopher, false);
		} else {
			requestChopstick(philosopher, false);
			requestChopstick(philosopher, true);
		}
		philosopher.setState(new Eating());
	}

	private void requestChopstick(Philosopher philosopher, boolean isLeft) {
		Chopstick chopstick = philosopher.getChopstick(isLeft);
		while (chopstick == null) {
			Response resp = philosopher.talkTo(new Request(), isLeft);
			chopstick = resp.getChopstick();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		chopstick.clean();
		philosopher.setChopstick(chopstick, isLeft);
	}

	@Override
	public void tick(Philosopher philosopher, double currentTime) {
		if (initTime == 0) {
			initTime = currentTime;
			return;
		}
		double timePassed = currentTime - initTime;
		if (timePassed > timeInterval) {
			philosopher.setState(new Dead());
		}
	}

}
