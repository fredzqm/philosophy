
public class Dead extends State {

	@Override
	public Response recieveRequestFrom(Philosopher philosopher, Request packet, boolean isLeft) {
		return new Response(philosopher.getChopstick(isLeft));
	}

	@Override
	public void switchedTo(Philosopher philosopher) {
		System.out.println("I am dead");
	}

}
