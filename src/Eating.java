public class Eating implements State {

	@Override
	public Response recieveRequestFrom(Philosopher philosopher, Request packet, boolean isLeft) {
		return new Response(null);
	}

	@Override
	public void switchedTo(Philosopher philosopher) {
		System.out.println("I am eating");
		philosopher.getChopstick(true).dirty();
		philosopher.getChopstick(false).dirty();
	}

}
