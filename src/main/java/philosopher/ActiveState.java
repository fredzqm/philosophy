package philosopher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import zookeeper.SideMap;

public class ActiveState extends PState {
	private Philosopher philospher;
	private DrinkState drinkState;
	private FoodState foodState;

	public ActiveState(Philosopher philosopher) {
		this.philospher = philosopher;
		this.switchDrinkState(new NotThirsty());
		this.switchFoodState(new NotHungry());
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

		@Override
		public void onStart() {
			super.onStart();
			if (Philosopher.automatic) {
				Timer.setTimeOut(100, 800, () -> {
					if (NotHungry.this.active && ActiveState.this.active)
						switchFoodState(new NotHungry());
				});
			}
		}
	}

	public class Hungry extends FoodState {

		@Override
		public void onStart() {
			super.onStart();
			if (Philosopher.automatic) {
				Timer.setTimeOut(100, 800, () -> {
					if (Hungry.this.active && ActiveState.this.active)
						switchFoodState(new NotHungry());
				});
			}
			List<Player> ls = new ArrayList<>();
			ls.add(philospher.getLeft());
			ls.add(philospher.getRight());
			Collections.sort(ls);
			while (!ls.get(0).holdingChopstick()) {
			}
			philospher.getMyself().hasOneChopstick();
			while (!ls.get(1).holdingChopstick()) {
			}
			switchFoodState(new Eating());
		}
	}

	public class Eating extends FoodState {

		@Override
		public void onStart() {
			super.onStart();
			philospher.getMyself().startEating();
			if (Philosopher.automatic) {
				Timer.setTimeOut(100, 800, () -> {
					if (Eating.this.active && ActiveState.this.active)
						switchFoodState(new NotHungry());
				});
			}
		}

		@Override
		public void onExit() {
			philospher.getMyself().finishEating();
			super.onExit();
		}

	}

	// ----------------------------------------------
	public class DrinkState extends PState {
		
	}

	public class NotThirsty extends DrinkState {
		
	}

	public class Thirsty extends DrinkState {

		@Override
		public void onStart() {
			super.onStart();
			while(philospher.bottleOccupied()) {}
			philospher.getTheBottle();
			switchDrinkState(new Drinking());
		}
	}

	public class Drinking extends DrinkState {
		
		@Override
		public void onStart() {
			super.onStart();
		}
		
		@Override
		public void onExit() {
			philospher.dropTheBottle();
			super.onExit();
		}
	}
}
