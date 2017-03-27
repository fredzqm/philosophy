
public class Dead implements State {

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

	@Override
	public void tick(Philosopher philosopher, double currentTime) {
		// TODO Auto-generated method stub
		System.out.println("I am dead");
	}

}
