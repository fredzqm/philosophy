import java.util.Scanner;

/**
 * 
 * @author fredzqm
 */
public class Philosopher {

	private static Side left, right;

	public static Side getRight() {
		return right;
	}

	public static Side getLeft() {
		return left;
	}

	public static Side resolveSide(String ip) {
		if (ip.equals(left.getIP())) {
			return left;
		} else if (ip.equals(right.getIP())) {
			return right;
		} else {
			throw new RuntimeException(
					String.format("Recieved connection from %s\n\tleft:&s\n\tright:%s\n", ip, left, right));
		}
	}

	public static void main(String[] args) {
		left = new Side(args[0], true);
		right = new Side(args[1], false);

		FoodManager foodManager = FoodManager.getInstance();
		BottleManager drinkManager = BottleManager.getInstance();

		Server server = new Server();
		server.addMessageReciever(foodManager);
		server.addMessageReciever(drinkManager);
		server.startServer();

		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		while (true) {
			String input = in.nextLine();
			switch (input) {
			case "thinking":
				foodManager.setFoodState(foodManager.new Thinking());
				drinkManager.setDrinkState(drinkManager.new NotThirsty());
				break;
			case "hungry":
				foodManager.setFoodState(foodManager.new Hungry());
				break;
			case "thirsty":
				drinkManager.setDrinkState(drinkManager.new Thirsty());
				break;
			case "notThirsty":
				drinkManager.setDrinkState(drinkManager.new NotThirsty());
			case "sleep":
				drinkManager.setDrinkState(drinkManager.new Sleep());
				break;
			default:
				System.out.println("Revieved event: " + input);
				break;
			}
		}
	}

}
