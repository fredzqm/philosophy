public class Hungry implements State {

	@Override
	public Response recieveRequestFrom(Philosopher philosopher, Request packet, boolean isLeft) {
		Chopstick chop = philosopher.getChopstick(isLeft);
		if (chop.isDirty()) {
			philosopher.setChopstick(null, isLeft);
			return new Response(chop);
		} else {
			return new Response(null);
		}
	}

	@Override
	public void switchedTo(Philosopher philosopher) {
		System.out.println("I am hugry");
		Chopstick left = philosopher.getChopstick(true);
		Chopstick right = philosopher.getChopstick(false);
		if (left == null) {
			requestChopstick(philosopher, true);
		}
		if (right == null) {
			requestChopstick(philosopher, false);
		}
		philosopher.setState(new Eating());
	}

	private void requestChopstick(Philosopher philosopher, boolean isLeft) {
		Response resp = null;
		Chopstick chopstick = null;
		while (chopstick == null) {
			resp = philosopher.talkTo(new Request(), isLeft);
			chopstick = resp.getChopstick();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		philosopher.setChopstick(chopstick, isLeft);
	}

}
