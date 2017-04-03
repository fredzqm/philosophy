
public class Dead implements State {

	@Override
	public void recieveMessageFrom(Philosopher philosopher, Message packet, Side isLeft) {

	}

	@Override
	public void switchedTo(Philosopher philosopher) {
		System.out.println("I am dead");
	}

}
