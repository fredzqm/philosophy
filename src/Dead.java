
public class Dead extends State {

	@Override
	public Response recieveRequestFrom(Philosopher philosopher, Request packet, boolean isLeft) {
		// TODO Auto-generated method stub
		return new Response(null);
	}

	@Override
	public void switchedTo(Philosopher philosopher) {
		// TODO Auto-generated method stub
		System.out.println("I am dead");

	}

}
