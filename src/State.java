public interface State {

	/**
	 * 
	 * @param philosopher
	 * @param packet
	 * @param isLeft
	 * @return get a request from a peer
	 */
	Response recieveRequestFrom(Philosopher philosopher, Request packet, boolean isLeft);

	/**
	 * executed whenever the philosopher switch to this state
	 * 
	 * @param philosopher
	 */
	void switchedTo(Philosopher philosopher);
	
	void tick(Philosopher philosopher, int currentTime);

	default int randomWithRange(int min, int max) {
		int range = (max - min) + 1;
		return (int) (Math.random() * range) + min;
	}
	
}
