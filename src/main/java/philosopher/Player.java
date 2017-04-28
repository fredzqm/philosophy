package philosopher;

import zookeeper.SideMap;

public class Player {
	private String ip;
	private SideMap zkmap;

	public Player(String ip) {
		this.ip = ip;
		this.zkmap = SideMap.getInstance();
	}

	public String getIP() {
		return ip;
	}

	public boolean holdingChopstick() {
		return this.zkmap.containsKey(ip + "/eating");
	}
}
