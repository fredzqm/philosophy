public class Thinking implements State {

	@Override
	public Response recieveRequestFrom(Philosopher philosopher, Request packet, boolean isLeft) {
		Chopstick leftChop = philosopher.getChopstick(isLeft);
		philosopher.setChopstick(null, isLeft);
		return new Response(leftChop);
	}

	@Override
	public void switchedTo(Philosopher philosopher) {
		System.out.println("I am thinking");
	}

}
