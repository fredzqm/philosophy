
public class Dead implements State {

	@Override
	public void recieveMessageFrom(Message packet, Side isLeft) {

	}

	@Override
	public void switchedTo() {
		System.out.println("I am dead");
	}

}
