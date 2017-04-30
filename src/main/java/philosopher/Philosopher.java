package philosopher;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import zookeeper.DataMonitorListener;
import zookeeper.IPFinder;
import zookeeper.SideMap;

/**
 * 
 * @author fredzqm
 */
public class Philosopher {
	public static boolean automatic = false;
	public static boolean verbose = true;

	private static final String READY = "Ready";
	private static final String REQUEST = "Request";
	public static final String END = "End";

	private Player myself, left, right;
	private String ip;
	private PState state;
	private SideMap map;
	private String playRequest;

	public Philosopher(String ip, String left, String right) {
		this.ip = ip;
		this.myself = new Player(ip);
		this.left = new Player(left);
		this.right = new Player(right);
		SideMap.getInstance().remove("bottle");
		this.getMyself().finishEating();
		this.map = SideMap.getInstance();

		String leftStr = this.getPlayString(left);
		this.map.remove(leftStr);
		this.map.addListener(leftStr, new GameListener(leftStr));
		String rightStr = this.getPlayString(right);
		this.map.remove(rightStr);
		this.map.addListener(rightStr, new GameListener(rightStr));
		this.switchTo(new ActiveState(this));
	}

	class GameListener implements DataMonitorListener {
		private String url;

		public GameListener(String url) {
			System.out.println(url);
			this.url = url;
		}

		@Override
		public void exists(String data) {
			System.out.println("url " + data);
			switch (data) {
			case REQUEST:
				setPlayRequest(url);
				if (Philosopher.this.state instanceof ActiveState) {
					swithToPlayState();
				}
				break;
			case READY:
				if (!(state instanceof Play))
					switchTo(new Play(Philosopher.this, url));
				break;
			case END:
				if (state instanceof Play)
					switchTo(new ActiveState(Philosopher.this));
				break;
			default:
				if (state instanceof Play) {
					Play p = (Play) state;
					p.getMessage(data);
				}
				break;
			}
		}

		@Override
		public void closing(int rc) {
			throw new RuntimeException();
		}

	}

	private void requestPlay(boolean isLeft) {
		if (isLeft) {
			this.map.put(getPlayString(getLeft().getIP()), REQUEST);
		} else {
			this.map.put(getPlayString(getRight().getIP()), REQUEST);
		}
		switchTo(new WaitToPlay());
	}

	public void swithToPlayState() {
		this.map.put(playRequest, READY);
		Philosopher.this.switchTo(new Play(this, playRequest));
	}

	public void switchTo(PState nextState) {
		if (this.state != null)
			this.state.onExit();
		this.state = nextState;
		this.state.onStart();
	}

	public String getPlayRequest() {
		return playRequest;
	}

	public void setPlayRequest(String playRequest) {
		this.playRequest = playRequest;
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

	public PState getState() {
		return state;
	}

	public static void main(String[] args) throws UnknownHostException, SocketException {
		String ip = IPFinder.getPublicIPv4();
		System.out.println("IP is " + ip + " left " + args[0] + " right " + args[1]);

		// need to remove all element in zookeeper when restart
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
			case "playLeft":
				ph.requestPlay(true);
				break;
			case "playRight":
				ph.requestPlay(false);
				break;
			default:
				if (ph.state instanceof Play) {
					Play p = (Play) ph.state;
					p.getInput(input);
				}
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

	private String getPlayString(String neighbor) {
		String playString = null;
		if (ip.compareTo(neighbor) > 0) {
			playString = "play" + ip + neighbor;
		} else {
			playString = "play" + neighbor + ip;
		}
		return playString;
	}

}
