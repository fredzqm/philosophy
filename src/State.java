public abstract class State {
	protected long initTime;
	private long timeInterval;

	public State() {
		initTime = 0;
		timeInterval = this.randomWithRange(10, 10000);
	}
	
	public void setInitialTime(long time) {
		this.initTime = time;
	}
	
	private int randomWithRange(int min, int max) {
		int range = (max - min) + 1;
		return (int) (Math.random() * range) + min;
	}

	/**
	 * 
	 * @param philosopher
	 * @param packet
	 * @param isLeft
	 * @return get a request from a peer
	 */
	abstract Response recieveRequestFrom(Philosopher philosopher, Request packet, boolean isLeft);

	/**
	 * executed whenever the philosopher switch to this state
	 * 
	 * @param philosopher
	 */
	abstract void switchedTo(Philosopher philosopher);

	/**
	 * a hook method to be called if the state is randomly expired
	 * 
	 * @param philosopher
	 */
	protected void timeOut(Philosopher philosopher) {}

	
	public void tick(Philosopher philosopher, long currentTime) {
		if (currentTime - initTime > timeInterval) {
			timeOut(philosopher);
		}
	}


}
