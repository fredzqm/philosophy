public class BottleManager {
	public boolean hasBottle = false;
	private static final int TIME_OUT = 100;
	private DrinkState drinkState;

	public BottleManager() {
		setDrinkState(new NotThirsty());
	}

	public void setDrinkState(DrinkState state) {
		this.drinkState = state;
		this.drinkState.onStart();
	}

	public DrinkState getDrinkState() {
		return drinkState;
	}

	public void recieveMessageFrom(Message packet, Side neighbor) {
		if (packet instanceof Message.Bottle) {
			neighbor.talkTo(new Message.ACKBottle());
			hasBottle = true;
			drinkState.getBottle(neighbor);
		} else if (packet instanceof Message.ACKBottle) {
			hasBottle = false;
		} else if (packet instanceof Message.BottleSearch) {
			if (hasBottle || drinkState.hasBottle())
				neighbor.talkTo(new Message.BottleHere());
		} else if (packet instanceof Message.BottleHere) {
			if (drinkState instanceof Thirsty) {
				((Thirsty)drinkState).angry = false;
			}
			neighbor.getTheOtherSide().talkTo(new Message.BottleHere());
		}
	}

	private void sendBottle(Side side) {
		side.talkTo(new Message.Bottle());
		hasBottle = true;
		Timer.setTimeOut(TIME_OUT, () -> {
			if (hasBottle)
				drinkState.getBottle(side.getTheOtherSide());
		});
	}

	public interface DrinkState {
		void getBottle(Side neighbor);

		boolean hasBottle();

		void onStart();
	}

	public class Drinking implements DrinkState {

		@Override
		public void getBottle(Side neighbor) {

		}

		@Override
		public void onStart() {
			System.out.println("I am drinking");
			Timer.setTimeOut(300, 1000, () -> {
				if (getDrinkState() == this) {
					setDrinkState(new NotThirsty());
					sendBottle(Philosopher.get().getRight());
				}
			});
		}

		@Override
		public boolean hasBottle() {
			return true;
		}

	}

	public class Thirsty implements DrinkState {
		private boolean angry;

		@Override
		public void getBottle(Side neighbor) {
			setDrinkState(new Drinking());
		}

		@Override
		public void onStart() {
			System.out.println("I am thirsty");
			Timer.setTimeOut(1000, () -> {
				getAngry();
			});
		}

		private void getAngry() {
			System.out.println("I am angry");
			if (getDrinkState() == this) {
				angry = true;
				Philosopher.get().getLeft().talkTo(new Message.BottleSearch());
				Philosopher.get().getRight().talkTo(new Message.BottleSearch());
				Timer.setTimeOut(10, () -> {
					if (angry) {
						setDrinkState(new Drinking());
					} else {
						Timer.setTimeOut(1000, () -> {
							getAngry();
						});
					}
				});
			}
		}

		@Override
		public boolean hasBottle() {
			return false;
		}

	}

	public class NotThirsty implements DrinkState {

		@Override
		public void getBottle(Side neighbor) {
			sendBottle(neighbor.getTheOtherSide());
		}

		@Override
		public void onStart() {
			System.out.println("I am not thirsty");
			Timer.setTimeOut(300, 1000, () -> {
				if (getDrinkState() == this) {
					setDrinkState(new Thirsty());
				}
			});
		}

		@Override
		public boolean hasBottle() {
			return false;
		}

	}
}
