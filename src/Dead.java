
public class Dead implements State {

	@Override
	public void recieveMessageFrom(Message packet, Side isLeft) {

	}

	@Override
	public void onStart() {
		System.out.println("I am dead");
	}

}
