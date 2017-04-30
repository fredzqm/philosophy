package philosopher;

import zookeeper.DataMonitorListener;
import zookeeper.SideMap;

public class GameListener implements Runnable {
	private Philosopher ph;
	private String playString;
	private String neighbor;
	// Synchronize?
	private SideMap map;

	public GameListener(Philosopher ph) {
		this.ph = ph;
		this.neighbor = ph.getLeft().getIP();
		this.playString = this.getPlayString(ph.getIP(), this.neighbor);
		this.map = SideMap.getInstance();
		this.map.addListener(playString, new DataMonitorListener() {
			
			@Override
			public void exists(String data) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void closing(int rc) {
				// TODO Auto-generated method stub
				
			}
		});;
	}

	@Override
	public void run() {
		while (true) {
			if (ph.getState() instanceof ActiveState) {
				if (map.containsKey(this.playString)) {
					ph.switchTo(new Play(ph, this.neighbor));
				}
			} else { // if in non activate state
				if (!map.containsKey(this.playString) && !(this.ph.getState() instanceof Play)) {
					this.ph.switchTo(new ActiveState(this.ph));
				}
			}
		}
	}

	public String getPlayString(String myIp, String neighbor) {
		String playString = null;
		if (myIp.compareTo(neighbor) > 0) {
			playString = "play" + myIp + neighbor;
		} else {
			playString = "play" + neighbor + myIp;
		}
		return playString;
	}

}
