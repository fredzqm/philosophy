public interface State {
	/**
	 * 
	 * @param philosopher
	 * @param packet
	 * @param isLeft
	 * @return get a request from a peer
	 */
	void recieveMessageFrom(Message packet, Side isLeft);

	/**
	 * executed whenever the philosopher switch to this state
	 * 
	 */
	void switchedTo();

}
