public class Hungry extends State {
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
	protected void timeOut(Philosopher philosopher) {
		philosopher.setState(new Dead());		
	}
	
}
