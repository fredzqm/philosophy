package philosopher;

import zookeeper.SideMap;

public class Player implements Comparable<Player> {
	private String ip;
	private SideMap zkmap;

	public Player(String ip) {
		this.ip = ip;
		this.zkmap = SideMap.getInstance();
	}

	public String getIP() {
		return ip;
	}

	@Override
	public int compareTo(Player o) {
		return this.ip.compareTo(o.ip);
	}

	public boolean holdingChopstick() {
		return this.zkmap.containsKey(getEastPath());
	}

	public void hasOneChopstick() {
		this.zkmap.put(getEastPath(), "half");
	}

	public void startEating() {
		this.zkmap.put(getEastPath(), "eating");
	}

	public void finishEating() {
		this.zkmap.remove(getEastPath());
	}

	private String getEastPath() {
		return ip + "eating";
	}
	
}
