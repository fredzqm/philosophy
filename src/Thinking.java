public class Thinking extends State {

	@Override
	public Response recieveRequestFrom(Philosopher philosopher, Request packet, boolean isLeft) {
		Chopstick chopstick = philosopher.getChopstick(isLeft);
		philosopher.setChopstick(null, isLeft);
		return new Response(chopstick);
	}

	@Override
	public void switchedTo(Philosopher philosopher) {
		System.out.println("I am thinking");
		Timer.setTimeOut((int) (Math.random() * 100 + 200), () -> {
			if (philosopher.getState() == this)
				philosopher.setState(new Hungry());
		});

		Chopstick l = philosopher.getChopstick(true);
		Chopstick r = philosopher.getChopstick(false);
		if (philosopher.isLeftFirst()) {
			if (r != null)
				r.clean();
			if (l != null)
				l.clean();
		} else {
			if (l != null)
				l.clean();
			if (r != null)
				r.clean();
		}
	}

}
