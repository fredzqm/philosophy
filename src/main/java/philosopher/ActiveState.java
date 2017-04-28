package philosopher;

public class ActiveState extends PState {
	private Philosopher philospher;
	private DrinkState drinkState;
	private FoodState foodState;

	public ActiveState(Philosopher philosopher) {
		this.philospher = philospher;
		this.drinkState = new NotThirsty();
		this.foodState = new NotHungry();
	}

	public ActiveState() {

	}

	public DrinkState getDrinkState() {
		return drinkState;
	}

	public void setDrinkState(DrinkState drinkState) {
		this.drinkState = drinkState;
	}

	public FoodState getFoodState() {
		return foodState;
	}

	public void setFoodState(FoodState foodState) {
		this.foodState = foodState;
	}

	// ----------------------------------------------
	public class FoodState extends PState {

	}

	public class NotHungry extends FoodState {

	}

	public class Hungry extends FoodState {

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
