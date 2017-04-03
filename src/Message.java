import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	

	public static class ChopstickReqest extends Message {
		private static final long serialVersionUID = 1L;
	}
	
	public static class BottleReqest extends Message {
		private static final long serialVersionUID = 1L;
	}
	
	public static class IsAvailableResponse extends Message {
		private static final long serialVersionUID = 1L;
		private final boolean isAvailable;
		
		public IsAvailableResponse(boolean available) {
			this.isAvailable = available;
		}

		public boolean isAvailable() {
			return isAvailable;
		}
	}
	
	public static class ChopstickResponse extends IsAvailableResponse {
		private static final long serialVersionUID = 1L;

		public ChopstickResponse(boolean available) {
			super(available);
		}
	}

	public static class BottleResponse extends IsAvailableResponse {
		private static final long serialVersionUID = 1L;

		public BottleResponse(boolean available) {
			super(available);
		}
	}

}
