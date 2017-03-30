public class Eating extends State {
	@Override
	public Response recieveRequestFrom(Philosopher philosopher, Request packet, boolean isLeft) {
		return new Response(null);
	}

	@Override
	public void switchedTo(Philosopher philosopher) {
		setTimeOutInterval((long) Math.random() * 100 + 100);
		
		System.out.println("I am eating");
		philosopher.getChopstick(true).dirty();
		philosopher.getChopstick(false).dirty();
	}

	@Override
	protected void timeOut(Philosopher philosopher) {
		philosopher.setState(new Thinking());
	}

}
