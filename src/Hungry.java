import java.util.HashSet;
import java.util.Set;

public class Hungry implements State {
	private final int REPEAT_TIME = 10;
	private Set<Boolean> has;
	private Runnable check;

	@Override
	public void recieveMessageFrom(Message packet, Side neighbor) {
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
			Philosopher.get().setFoodState(new Eating());
	}

	@Override
	public void onStart() {
		System.out.println("I am hungry");
		has = new HashSet<>();
		Timer.setTimeOut(50000, this, () -> {
			Philosopher.get().setFoodState(new Dead());
		});
		Philosopher.get().getRight().talkTo(new Message.ChopstickReqest());
		Philosopher.get().getLeft().talkTo(new Message.ChopstickReqest());

		check = () -> {
			if (!has.contains(false))
				Philosopher.get().getRight().talkTo(new Message.ChopstickReqest());
			if (!has.contains(true))
				Philosopher.get().getLeft().talkTo(new Message.ChopstickReqest());
			Timer.setTimeOut(REPEAT_TIME, Hungry.this, check);
		};
		Timer.setTimeOut(REPEAT_TIME, this, check);
	}

}
