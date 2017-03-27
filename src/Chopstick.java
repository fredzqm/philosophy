import java.io.Serializable;

public class Chopstick implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private boolean isDirty;

	public Chopstick() {
		this.isDirty = true;
	}

	public void clean() {
		this.isDirty = false;
	}

	public void dirty() {
		this.isDirty = true;
	}

	public boolean isDirty() {
		return this.isDirty;
	}
}
