package philosopher;

import zookeeper.SideMap;

public class Play extends PState {
	private Philosopher ph;
	private String neighbor;

	public Play(Philosopher ph, String neighbor) {
		this.ph = ph;
		this.neighbor = neighbor;
	}

	@Override
	public void onStart() {
		this.ph.getMyself().finishEating();
		this.ph.dropTheBottle();
		if (Philosopher.verbose){
			System.out.println("Playing");
		}
		super.onStart();
		if (Philosopher.automatic){
			Timer.setTimeOut(100, 800, () -> {
				ph.switchTo(new ActiveState(ph));
			});
		}
	}

	public void play() {
		SideMap.getInstance().put(getPlayString(), "-");
	}

	public String getPlayString() {
		String playString = null;
		if (ph.getIP().compareTo(neighbor) > 0) {
			playString = "play" + ph.getIP() + neighbor;
		} else {
			playString = "play" + neighbor + ph.getIP();
		}
		return playString;
	}
}
