public class Eating implements State {

	@Override
	public void recieveMessageFrom(Philosopher philosopher, Message packet, Side neighbor) {
		if (packet instanceof Message.ChopstickReqest) {
			neighbor.talkTo(new Message.ChopstickResponse(false));
		}
	}

	@Override
	public void switchedTo(Philosopher philosopher) {
		System.out.println("I am eating");
		Timer.setTimeOut((int) (Math.random() * 100 + 100), () -> {
			if (philosopher.getState() == this)
				philosopher.setState(new Thinking());
		});
	}
}
