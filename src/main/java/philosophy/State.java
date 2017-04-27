package philosophy;

public interface State extends MessageReciever {
	/**
	 * executed whenever the philosopher switch to this state
	 * 
	 */
	void onStart();
}