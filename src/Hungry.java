public class Hungry extends State {
	
	@Override
	void recieveMessageFrom(Philosopher philosopher, Message packet, Neighbor isLeft) {

	}
	
//	@Override
//	public void recieveMessageFrom(Philosopher philosopher, Message packet, boolean isLeft) {
//		Chopstick chop = philosopher.getChopstick(isLeft);
//		if (chop != null && chop.isDirty()) {
//			philosopher.setChopstick(null, isLeft);
////			return new Response(chop);
//		} else {
////			return new Response(null);
//		}
//	}

	@Override
	public void switchedTo(Philosopher philosopher) {
		System.out.println("I am hugry");
		Timer.setTimeOut(1000, () -> {
			if (philosopher.getState() == this)
				philosopher.setState(new Hungry());
		});

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
//			philosopher.talkTo(new Message(), isLeft);
//			chopstick = resp.getChopstick();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		chopstick.clean();
		philosopher.setChopstick(chopstick, isLeft);
	}

}
