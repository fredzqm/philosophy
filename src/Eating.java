public class Eating implements State {

	@Override
	public void recieveMessageFrom(Message packet, Side neighbor) {
		if (packet instanceof Message.ChopstickReqest) {
			neighbor.talkTo(new Message.ChopstickResponse(false));
		}
	}

	@Override
	public void switchedTo() {
		System.out.println("I am eating");
		Timer.setTimeOut(100, 200, this, () -> {
				Philosopher.get().setState(new Thinking());
		});
	}
}
