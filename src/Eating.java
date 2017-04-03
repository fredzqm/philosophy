public class Eating extends State {
	@Override
	public void recieveMessageFrom(Philosopher philosopher, Message packet, boolean isLeft) {
//		return new Response(null);
	}

	@Override
	public void switchedTo(Philosopher philosopher) {
		Timer.setTimeOut((int) (Math.random() * 100 + 100), () -> {
			if (philosopher.getState() == this)
				philosopher.setState(new Thinking());
		});
		System.out.println("I am eating");
		philosopher.getChopstick(true).dirty();
		philosopher.getChopstick(false).dirty();
	}

}
