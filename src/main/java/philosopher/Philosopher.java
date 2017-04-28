package philosopher;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import zookeeper.IPFinder;
import zookeeper.SideMap;

/**
 * 
 * @author fredzqm
 */
public class Philosopher {
	public static boolean automatic = false;
	public static boolean verbose = true;
	private Player myself, left, right;
	private String ip;
	private PState state;

	public Philosopher(String ip, String left, String right) {
		this.ip = ip;
		this.myself = new Player(ip);
		this.left = new Player(left);
		this.right = new Player(right);
		this.switchTo(new ActiveState(this));
	}

	public void switchTo(PState nextState) {
		if (this.state != null)
			this.state.onExit();
		this.state = nextState;
		this.state.onStart();
	}

	public String getIP() {
		return ip;
	}

	public Player getMyself() {
		return myself;
	}

	public Player getRight() {
		return right;
	}

	public Player getLeft() {
		return left;
	}

	public static void main(String[] args) throws UnknownHostException, SocketException {
		String ip = IPFinder.getPublicIPv4();
		System.out.println("IP is " + ip);
		Philosopher ph = new Philosopher(ip, args[0], args[1]);

		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		while (true) {
			String input = in.nextLine();
			switch (input) {
			case "thinking":
				// foodManager.setFoodState(foodManager.new Thinking());
				// drinkManager.setDrinkState(drinkManager.new NotThirsty());
				break;
			case "hungry":
				// foodManager.setFoodState(foodManager.new Hungry());
				break;
			case "thirsty":
				// drinkManager.setDrinkState(drinkManager.new Thirsty());
				break;
			case "notThirsty":
				// drinkManager.setDrinkState(drinkManager.new NotThirsty());
				break;
			case "sleep":
				// drinkManager.setDrinkState(drinkManager.new Sleep());
				break;
			default:
				// System.out.println("Revieved event: " + input + " current
				// state: " + drinkManager.getDrinkState());
				break;
			}
		}
	}

	public boolean bottleOccupied() {
		return SideMap.getInstance().containsKey("bottle");
	}

	public void getTheBottle() {
		SideMap.getInstance().put("bottle", getIP());
	}

	public void dropTheBottle() {
		SideMap map = SideMap.getInstance();
		if (getIP().equals(map.get("bottle")))
			map.remove("bottle");
	}


}
