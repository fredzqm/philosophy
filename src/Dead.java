
public class Dead extends State {

	@Override
	public void recieveMessageFrom(Philosopher philosopher, Message packet, boolean isLeft) {
//		return new Response(philosopher.getChopstick(isLeft));
	}

	@Override
	public void switchedTo(Philosopher philosopher) {
		System.out.println("I am dead");
	}

}
