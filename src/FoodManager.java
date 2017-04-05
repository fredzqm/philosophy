import java.util.HashSet;
import java.util.Set;

public class FoodManager implements MessageReciever {
	private State foodState;

	public FoodManager() {
		this.setFoodState(new Thinking());
	}

	public void setFoodState(State state) {
		this.foodState = state;
		this.foodState.onStart();
	}

	public void recieveMessageFrom(Message packet, Side neighbor) {
		this.foodState.recieveMessageFrom(packet, neighbor);
	}

	public static class ChopstickReqest extends Message {
		private static final long serialVersionUID = 1L;

		@Override
		public String toString() {
			return "Do you have the chopstick?";
		}
	}

	public static class ChopstickResponse extends Message {
		private static final long serialVersionUID = 1L;
		private final boolean isAvailable;

		public ChopstickResponse(boolean available) {
			this.isAvailable = available;
		}

		public boolean isAvailable() {
			return isAvailable;
		}

		@Override
		public String toString() {
			if (isAvailable)
				return "I am not using";
			else
				return "I am using it";
		}
	}

	public class Eating implements State {

		@Override
		public void recieveMessageFrom(Message packet, Side neighbor) {
			if (packet instanceof ChopstickReqest) {
				neighbor.talkTo(new ChopstickResponse(false));
			}
		}

		@Override
		public void onStart() {
			System.out.println("I am eating");
			Timer.setTimeOut(1000, 2000, () -> {
				if (foodState == this)
					setFoodState(new Thinking());
			});
		}
	}

	public class Thinking implements State {

		@Override
		public void recieveMessageFrom(Message packet, Side neighbor) {
			if (packet instanceof ChopstickReqest) {
				neighbor.talkTo(new ChopstickResponse(true));
			}
		}

		@Override
		public void onStart() {
			System.out.println("I am thinking");
			Timer.setTimeOut(1000, 3000, () -> {
				if (foodState == this)
					setFoodState(new Hungry());
			});
		}

	}

	public class Hungry implements State {
		private final int REPEAT_TIME = 10;
		private Set<Boolean> has;

		@Override
		public void recieveMessageFrom(Message packet, Side neighbor) {
			if (packet instanceof ChopstickReqest) {
				if (Math.random() > 0.5) {
					neighbor.talkTo(new ChopstickResponse(false));
					has.add(neighbor.isLeft());
				} else {
					neighbor.talkTo(new ChopstickResponse(true));
					has.remove(neighbor.isLeft());
				}
			} else if (packet instanceof ChopstickResponse) {
				ChopstickResponse resp = (ChopstickResponse) packet;
				if (resp.isAvailable()) {
					has.add(neighbor.isLeft());
				}
			}
			if (has.size() == 2)
				setFoodState(new Eating());
		}

		@Override
		public void onStart() {
			System.out.println("I am hungry");
			has = new HashSet<>();
			Timer.setTimeOut(50000, () -> {
				if (foodState == this)
					setFoodState(new Dead());
			});
			Philosopher.getRight().talkTo(new ChopstickReqest());
			Philosopher.getLeft().talkTo(new ChopstickReqest());

			setUpCheckTimer();
		}

		private void setUpCheckTimer() {
			Timer.setTimeOut(REPEAT_TIME, () -> {
				if (foodState == this) {
					if (!has.contains(false))
						Philosopher.getRight().talkTo(new ChopstickReqest());
					if (!has.contains(true))
						Philosopher.getLeft().talkTo(new ChopstickReqest());
					setUpCheckTimer();
				}
			});
		}

	}

	public class Sleep implements State {
		@Override
		public void recieveMessageFrom(Message packet, Side isLeft) {

		}

		@Override
		public void onStart() {
			System.out.println("I am sleeping");
			Timer.setTimeOut(0, 1000, () -> {
				if (foodState == this)
					setFoodState(new Thinking());
			});
		}
	}

	public class Dead implements State {

		@Override
		public void recieveMessageFrom(Message packet, Side isLeft) {

		}

		@Override
		public void onStart() {
			System.out.println("I am dead");
		}

	}

}
