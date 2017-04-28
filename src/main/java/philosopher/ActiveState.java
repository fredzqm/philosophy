package philosopher;

import philosophyOld.FoodManager.Thinking;

public class ActiveState extends PState {
	private Philosopher philospher;
	private DrinkState drinkState;
	private FoodState foodState;

	public ActiveState(Philosopher philosopher) {
		this.philospher = philospher;
		this.switchDrinkState(new NotThirsty());
		this.switchFoodState(new NotHungry());
	}

	public ActiveState() {

	}

	public DrinkState getDrinkState() {
		return drinkState;
	}

	public void switchDrinkState(DrinkState nextState) {
		if (this.drinkState != null)
			this.drinkState.onExit();
		this.drinkState = nextState;
		this.drinkState.onStart();
	}

	public FoodState getFoodState() {
		return foodState;
	}

	public void switchFoodState(FoodState nextState) {
		if (this.foodState != null)
			this.foodState.onExit();
		this.foodState = nextState;
		this.foodState.onStart();
	}

	// ----------------------------------------------
	public class FoodState extends PState {

	}

	public class NotHungry extends FoodState {

	}

	public class Hungry extends FoodState {

		@Override
		public void onStart() {
			super.onStart();
			Timer.setTimeOut(100, 800, () -> {
				if (foodState == this)
					switchFoodState(new NotHungry());
			});
			
			Player left = philospher.getLeft();
			while (!left.holdingChopstick()) {
				
			}
			Player right = philospher.getRight();
			while (!right.holdingChopstick()) {
				
			}
			// while (left.isEating() || right.isEating()) {
			//
			// }



		}
	}

	public class Eating extends FoodState {

	}

	// ----------------------------------------------
	public class DrinkState extends PState {

	}

	public class NotThirsty extends DrinkState {

	}

	public class Thirsty extends FoodState {

	}

	public class Drinking extends FoodState {

	}
}
