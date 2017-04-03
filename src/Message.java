import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	public static class ChopstickReqest extends Message {
		private static final long serialVersionUID = 1L;
	}

	public static class ChopstickResponse extends Message {
		private static final long serialVersionUID = 1L;
		private final boolean isAvailable;

		public ChopstickResponse(boolean available) {
			this.isAvailable = available;
		}

		public boolean isAvailable() {
			return isAvailable;
		}
	}

	public static class Bottle extends Message {
		private static final long serialVersionUID = 1L;
	}

	public static class ACKBottle extends Message {
		private static final long serialVersionUID = 1L;
	}

	public static class BottleSearch extends Message {
		private static final long serialVersionUID = 1L;
		private int ttl;
		
		public BottleSearch(int ttl) {
			this.ttl = ttl;
		}

		public int getTTL() {
			return ttl;
		}
	}

	public static class BottleHere extends Message {
		private static final long serialVersionUID = 1L;
		private int ttl;
		
		public BottleHere(int ttl) {
			this.ttl = ttl;
		}

		public int getTTL() {
			return ttl;
		}
	}
}
