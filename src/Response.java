import java.io.Serializable;

public class Response implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Chopstick chopstick;

	public Response(Chopstick granted) {
		this.chopstick = granted;
		if (chopstick != null)
			this.chopstick.clean();
	}

	public Chopstick getChopstick() {
		return chopstick;
	}
	
	@Override
	public String toString() {
		return "Reponse: " + chopstick;
	}

}
