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
		
		this.dropTheBottle();
		this.getMyself().finishEating();
		
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
		System.out.println("IP is " + ip+ " left "+ args[0] + " right " + args[1]);
		Philosopher ph = new Philosopher(ip, args[0], args[1]);

		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		while (true) {
			String input = in.nextLine();
			switch (input) {
			case "nhungry":
				ph.notHungry();
				break;
			case "hungry":
				ph.hungry();
				break;
			case "thirsty":
				ph.thirsty();
				break;
			case "nthirsty":
				ph.notThirsty();
				break;
			case "sleep":
				ph.sleep();
				break;
			default:
				break;
			}
		}
	}

	private void sleep() {
		switchTo(new Sleep(this));
	}

	private void thirsty() {
		if (!(this.state instanceof ActiveState)) {
			this.switchTo(new ActiveState(this));
		}
		ActiveState x = (ActiveState) this.state;
		x.switchDrinkState(x.new Thirsty());
	}

	private void notThirsty() {
		if (!(this.state instanceof ActiveState)) {
			this.switchTo(new ActiveState(this));
		}
		ActiveState x = (ActiveState) this.state;
		x.switchDrinkState(x.new NotThirsty());
	}

	private void notHungry() {
		if (!(this.state instanceof ActiveState)) {
			this.switchTo(new ActiveState(this));
		}
		ActiveState x = (ActiveState) this.state;
		x.switchFoodState(x.new NotHungry());
	}

	private void hungry() {
		if (!(this.state instanceof ActiveState)) {
			this.switchTo(new ActiveState(this));
		}
		ActiveState x = (ActiveState) this.state;
		x.switchFoodState(x.new Hungry());
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
