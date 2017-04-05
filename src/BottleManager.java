public class BottleManager implements MessageReciever {
	public final int NUM_OF_NODE = 5;

	public boolean hasBottle = false;
	private static final int SEND_BOTTLE_TIME_OUT = 100;
	private AWAKEDrinkState drinkState;

	public BottleManager() {
		setDrinkState(new NotThirsty());
	}

	public void setDrinkState(AWAKEDrinkState state) {
		this.drinkState = state;
		this.drinkState.onStart();
	}

	@Override
	public void recieveMessageFrom(Message packet, Side neighbor) {
		drinkState.recieveMessageFrom(packet, neighbor);
	}

	public State getDrinkState() {
		return drinkState;
	}

	public static class Bottle extends Message {
		private static final long serialVersionUID = 1L;
	}

	public static class ACKBottle extends Message {
		private static final long serialVersionUID = 1L;

		@Override
		public String toString() {
			return "Here is the bottle";
		}
	}

	public static class BottleSearch extends Message {
		private static final long serialVersionUID = 1L;
		private int ttl;

		public BottleSearch(int ttl) {
			this.ttl = ttl;
		}

		public int getTTL() {
			return ttl;
		}

		@Override
		public String toString() {
			return "Where is the bottle?";
		}
	}

	public static class BottleHere extends Message {
		private static final long serialVersionUID = 1L;
		private int ttl;

		public BottleHere(int ttl) {
			this.ttl = ttl;
		}

		public int getTTL() {
			return ttl;
		}

		@Override
		public String toString() {
			return "I have the bottle";
		}
	}

	public abstract class AWAKEDrinkState implements State {

		@Override
		public void recieveMessageFrom(Message packet, Side neighbor) {
			if (packet instanceof Bottle) {
				neighbor.talkTo(new ACKBottle());
				hasBottle = true;
				recieveBottle(neighbor);
			} else if (packet instanceof ACKBottle) {
				reciveBottleACK();
			} else if (packet instanceof BottleSearch) {
				int ttl = ((BottleSearch) packet).getTTL() - 1;
				if (hasBottle)
					neighbor.talkTo(new BottleHere(NUM_OF_NODE));
				else if (ttl != 0)
					neighbor.getTheOtherSide().talkTo(new BottleSearch(ttl));
			} else if (packet instanceof BottleHere) {
				int ttl = ((BottleHere) packet).getTTL() - 1;
				if (ttl != 0)
					neighbor.getTheOtherSide().talkTo(new BottleHere(ttl));
				recieveBottleHere();
			}
		}

		protected void sendBottle(Side neighbor) {
			neighbor.getTheOtherSide().talkTo(new Bottle());
			Timer.setTimeOut(SEND_BOTTLE_TIME_OUT, () -> {
				if (hasBottle) {
					drinkState.recieveBottle(neighbor.getTheOtherSide());
				}
			});
		}

		public void reciveBottleACK() {
			hasBottle = false;
		}

		public abstract void recieveBottle(Side neighbor);

		public void recieveBottleHere() {
		}

	}

	public class Drinking extends AWAKEDrinkState {
		private Side nextDir;

		public Drinking(Side from) {
			this.nextDir = from;
		}

		@Override
		public void onStart() {
			System.out.println("I am drinking");
			hasBottle = true;
			Timer.setTimeOut(300, 600, () -> {
				if (getDrinkState() == this) {
					setDrinkState(new NotThirsty());
					sendBottle(nextDir);
				}
			});
		}

		@Override
		public void reciveBottleACK() {
			System.out.println("I am drinking, but recieved a bottleACK");
		}

		@Override
		public void recieveBottle(Side neighbor) {
			System.out.println("I am drinking, but recieved a bottle from " + neighbor);
		}

	}

	public class Sleep extends AWAKEDrinkState {

		@Override
		public void onStart() {
			System.out.println("I am sleeping");
		}

		@Override
		public void recieveMessageFrom(Message packet, Side neighbor) {
		}

		@Override
		public void recieveBottle(Side neighbor) {

		}

		public void reciveBottleACK() {

		}

		public void recieveBottleHere() {

		}
	}

	public class Thirsty extends AWAKEDrinkState {
		private boolean angry;

		@Override
		public void onStart() {
			System.out.println("I am thirsty");
			setAngryTimer();
		}

		private void setAngryTimer() {
			angry = true;
			Timer.setTimeOut(100, 200, () -> {
				if (getDrinkState() == this) {
					if (angry) {
						Philosopher.getLeft().talkTo(new BottleSearch(NUM_OF_NODE));
						Philosopher.getRight().talkTo(new BottleSearch(NUM_OF_NODE));
						Timer.setTimeOut(10, () -> {
							if (getDrinkState() == this && angry) {
								System.out.print("I am angry and ");
								setDrinkState(new Drinking(Philosopher.getRight()));
							} else {
								setAngryTimer();
							}
						});
					} else {
						setAngryTimer();
					}
				}
			});
		}

		@Override
		public void recieveBottle(Side neighbor) {
			setDrinkState(new Drinking(neighbor.getTheOtherSide()));
		}

		@Override
		public void recieveBottleHere() {
			angry = false;
		}
	}

	public class NotThirsty extends AWAKEDrinkState {

		@Override
		public void onStart() {
			System.out.println("I am not thirsty");
			Timer.setTimeOut(300, 1000, () -> {
				if (getDrinkState() == this) {
					setDrinkState(new Thirsty());
				}
			});
		}

		public void recieveBottle(Side neighbor) {
			sendBottle(neighbor.getTheOtherSide());
		}

	}
}
