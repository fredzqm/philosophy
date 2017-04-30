package philosopher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import zookeeper.SideMap;

public class ActiveState extends PState {
	private Philosopher philospher;
	private DrinkState drinkState;
	private FoodState foodState;

	public ActiveState(Philosopher philospher) {
		this.philospher = philospher;
		this.drinkState = new NotThirsty();
		this.foodState = new NotHungry();
	}

	@Override
	public void onStart() {
		super.onStart();
		if (this.philospher.getPlayRequest() != null) {
			this.philospher.swithToPlayState();
		}
		this.drinkState.onStart();
		this.foodState.onStart();
	}

	@Override
	public void onExit() {
		this.drinkState.onExit();
		this.foodState.onExit();
		this.philospher.getMyself().finishEating();
		this.philospher.dropTheBottle();
		super.onExit();
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

		public void speak(String message) {
			if (Philosopher.verbose)
				System.out.println(message);
		}

	}

	public class NotHungry extends FoodState {

		@Override
		public void onStart() {
			super.onStart();
			super.speak("Not hungry");
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
			super.speak("Hungry");
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
			while (ls.get(0).holdingChopstick()) {
			}
			philospher.getMyself().hasOneChopstick();
			while (ls.get(1).holdingChopstick()) {
			}
			switchFoodState(new Eating());
		}
	}

	public class Eating extends FoodState {

		@Override
		public void onStart() {
			super.onStart();
			super.speak("Eating");
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
		public void speak(String message) {
			if (Philosopher.verbose)
				System.out.println(message);
		}
	}

	public class NotThirsty extends DrinkState {
		@Override
		public void onStart() {
			super.onStart();
			super.speak("Not thristy");
			if (Philosopher.automatic) {
				Timer.setTimeOut(100, 800, () -> {
					if (NotThirsty.this.active)
						switchDrinkState(new Thirsty());
				});
			}
		}

	}

	public class Thirsty extends DrinkState {

		@Override
		public void onStart() {
			super.onStart();
			super.speak("Thirsty");

			while (philospher.bottleOccupied()) {
			}
			philospher.getTheBottle();
			switchDrinkState(new Drinking());
		}
	}

	public class Drinking extends DrinkState {

		@Override
		public void onStart() {
			super.onStart();
			super.speak("Drinking");
			if (Philosopher.automatic) {
				Timer.setTimeOut(100, 800, () -> {
					if (Drinking.this.active)
						switchDrinkState(new NotThirsty());
				});
			}
		}

		@Override
		public void onExit() {
			philospher.dropTheBottle();
			super.onExit();
		}
	}
}
