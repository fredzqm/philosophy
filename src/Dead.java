
public class Dead extends State {

	@Override
	void recieveMessageFrom(Philosopher philosopher, Message packet, Neighbor isLeft) {

	}

	@Override
	public void switchedTo(Philosopher philosopher) {
		System.out.println("I am dead");
	}

}
