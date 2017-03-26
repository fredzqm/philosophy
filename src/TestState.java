
public class TestState implements State {

	@Override
	public Response recieveRequestFrom(Philosopher philosopher, Request packet, boolean isLeft) {
		return new Response("Recieved " + packet.str);
	}

	@Override
	public void switchedTo(Philosopher philosopher) {

	}

}
