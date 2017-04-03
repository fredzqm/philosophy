public class Eating implements State {

	@Override
	public void recieveMessageFrom(Message packet, Side neighbor) {
		if (packet instanceof Message.ChopstickReqest) {
			neighbor.talkTo(new Message.ChopstickResponse(false));
		}
	}

	@Override
	public void onStart() {
		System.out.println("I am eating");
		Timer.setTimeOut(1000, 2000, this, () -> {
				Philosopher.get().setFoodState(new Thinking());
		});
	}
}
