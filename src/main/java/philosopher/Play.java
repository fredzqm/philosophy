package philosopher;

import zookeeper.SideMap;

public class Play extends PState {
	private Philosopher ph;
	private String url;

	public Play(Philosopher ph, String neighbor) {
		this.ph = ph;
		this.url = neighbor;
	}

	@Override
	public void onStart() {
		super.onStart();
		this.play();
	}
	
	@Override
	public void onExit(){
		super.onExit();
		ph.setPlayRequest(null);
		SideMap.getInstance().put(url, Philosopher.END);
	}

	public void play() {
		if (Philosopher.verbose){
			System.out.println("Playing");
		}
		if (Philosopher.automatic){
			Timer.setTimeOut(100, 800, () -> {
				ph.switchTo(new ActiveState(ph));
			});
		}
	}

	public String getPlayString() {
		String playString = null;
		if (ph.getIP().compareTo(url) > 0) {
			playString = "play" + ph.getIP() + url;
		} else {
			playString = "play" + url + ph.getIP();
		}
		return playString;
	}

	public void getInput(String input) {
		SideMap.getInstance().put(url, input);
	}

	public void getMessage(String data) {
		System.out.println("Recieved messaged: "+ data);
	}
}
