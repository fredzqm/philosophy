
public class Dead extends State {

	@Override
	void recieveMessageFrom(Philosopher philosopher, Message packet, Side isLeft) {

	}

	@Override
	public void switchedTo(Philosopher philosopher) {
		System.out.println("I am dead");
	}

}
