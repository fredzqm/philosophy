import java.util.HashSet;
import java.util.Set;

public class FoodManager {
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

	
	public interface State {
		/**
		 * 
		 * @param philosopher
		 * @param packet
		 * @param isLeft
		 * @return get a request from a peer
		 */
		void recieveMessageFrom(Message packet, Side neighbor);

		/**
		 * executed whenever the philosopher switch to this state
		 * 
		 */
		void onStart();
	}

	
	public class Eating implements State {

		@Override
		public void recieveMessageFrom(Message packet, Side neighbor) {
			if (packet instanceof Message.ChopstickReqest) {
				neighbor.talkTo(new Message.ChopstickResponse(false));
			}
		}

		@Override
		public void onStart() {
			System.out.println("I am eating");
			Timer.setTimeOut(1000, 2000, () -> {
				if (foodState != this)
					setFoodState(new Thinking());
			});
		}
	}

	public class Thinking implements State {

		@Override
		public void recieveMessageFrom(Message packet, Side neighbor) {
			if (packet instanceof Message.ChopstickReqest) {
				neighbor.talkTo(new Message.ChopstickResponse(true));
			}
		}

		@Override
		public void onStart() {
			System.out.println("I am thinking");
			Timer.setTimeOut(1000, 3000, () -> {
				if (foodState != this)
					setFoodState(new Hungry());
			});
		}

	}

	public class Hungry implements State {
		private final int REPEAT_TIME = 10;
		private Set<Boolean> has;

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
				setFoodState(new Eating());
		}

		@Override
		public void onStart() {
			System.out.println("I am hungry");
			has = new HashSet<>();
			Timer.setTimeOut(50000, () -> {
				if (foodState != this)
					setFoodState(new Dead());
			});
			Philosopher.getRight().talkTo(new Message.ChopstickReqest());
			Philosopher.getLeft().talkTo(new Message.ChopstickReqest());

			setUpCheckTimer();
		}

		private void setUpCheckTimer() {
			Timer.setTimeOut(REPEAT_TIME, () -> {
				if (foodState != this) {
					if (!has.contains(false))
						Philosopher.getRight().talkTo(new Message.ChopstickReqest());
					if (!has.contains(true))
						Philosopher.getLeft().talkTo(new Message.ChopstickReqest());
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
				if (foodState != this)
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
