import java.util.HashSet;
import java.util.Set;

public class Hungry implements State {
	private final int REPEAT_TIME = 10;
	private Set<Boolean> has;
	private Runnable check;
	
	@Override
	public void recieveMessageFrom(Philosopher philosopher, Message packet, Side neighbor) {
		if (packet instanceof Message.ChopstickReqest) {
			if (Math.random() > 0.5) {
				neighbor.talkTo(new Message.ChopstickResponse(false));
				has.add(neighbor.isLeft());
			} else {
				neighbor.talkTo(new Message.ChopstickResponse(true));
				has.remove(neighbor.isLeft());
			}
		} else if (packet instanceof Message.ChopstickResponse) {
			Message.ChopstickResponse resp = (Message.ChopstickResponse) packet;
			if (resp.isAvailable()) {
				has.add(neighbor.isLeft());
			}
		}
		if (has.size() == 2)
			philosopher.setState(new Eating());
	}

	@Override
	public void switchedTo(Philosopher philosopher) {
		System.out.println("I am hugry");
		has = new HashSet<>();
		Timer.setTimeOut(10000, () -> {
			if (philosopher.getState() == this)
				philosopher.setState(new Dead());
		});
		philosopher.getRight().talkTo(new Message.ChopstickReqest());
		philosopher.getLeft().talkTo(new Message.ChopstickReqest());
		
		check = () -> {
			if (philosopher.getState() == this) {
				if (!has.contains(false))
					philosopher.getRight().talkTo(new Message.ChopstickReqest());
				if (!has.contains(true))
					philosopher.getLeft().talkTo(new Message.ChopstickReqest());
				Timer.setTimeOut(REPEAT_TIME, check);
			}
		};
		Timer.setTimeOut(REPEAT_TIME, check);
	}
	

}
