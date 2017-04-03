
public class Sleep implements State {

	@Override
	public void recieveMessageFrom(Message packet, Side isLeft) {

	}

	@Override
	public void onStart() {
		System.out.println("I am sleeping");
		Timer.setTimeOut(0, 500, this, () -> {
			Philosopher.get().setFoodState(new Thinking());
		});
	}

}
