public class Thinking implements State {

	@Override
	public void recieveMessageFrom(Message packet, Side neighbor) {
		if (packet instanceof Message.ChopstickReqest) {
			neighbor.talkTo(new Message.ChopstickResponse(true));
		}
	}

	@Override
	public void onStart() {
		System.out.println("I am thinking");
		Timer.setTimeOut(100, 300, this, () -> {
			Philosopher.get().setFoodState(new Hungry());
		});
	}

}
