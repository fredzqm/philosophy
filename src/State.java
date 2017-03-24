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

}
