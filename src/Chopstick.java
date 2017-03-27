import java.io.Serializable;

public class Chopstick implements Serializable, Comparable<Chopstick> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean isDirty;
	private double id;

	public Chopstick() {
		this.isDirty = true;
		this.id = Math.random();
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

	@Override
	public int compareTo(Chopstick o) {
		if (o.id > id)
			return 1;
		return -1;
	}
}
