public abstract class State {
	/**
	 * 
	 * @param philosopher
	 * @param packet
	 * @param isLeft
	 * @return get a request from a peer
	 */
	abstract void recieveMessageFrom(Philosopher philosopher, Message packet, Side isLeft);

	/**
	 * executed whenever the philosopher switch to this state
	 * 
	 * @param philosopher
	 */
	abstract void switchedTo(Philosopher philosopher);

}
