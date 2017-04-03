public class Thinking implements State {

	@Override
	public void recieveMessageFrom(Philosopher philosopher, Message packet, Side neighbor) {
		if (packet instanceof Message.ChopstickReqest) {
			neighbor.talkTo(new Message.ChopstickResponse(true));
		}
	}

	@Override
	public void switchedTo(Philosopher philosopher) {
		System.out.println("I am thinking");
		Timer.setTimeOut((int) (Math.random() * 100 + 200), () -> {
			if (philosopher.getState() == this)
				philosopher.setState(new Hungry());
		});
	}

}
