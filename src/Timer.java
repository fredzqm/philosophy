import java.util.PriorityQueue;

public class Timer {
	private final static int TIME_MULTIPLIER = 10;
	private final static PriorityQueue<TimeOutEvent> timeOuts;
	private static int timer;

	public static int getTime() {
		return timer;
	}

	public static void setTimeOut(int min, int max, State state, Runnable callback) {
		setTimeOut((int) (Math.random() * (max - min)) + min, state, callback);
	}

	public static void setTimeOut(int time, State state, Runnable callback) {
		timeOuts.add(new TimeOutEvent(time, state, callback));
	}

	public static class TimeOutEvent implements Comparable<TimeOutEvent>, Runnable {
		private int triggetTime;
		private State state;
		private Runnable callback;

		public TimeOutEvent(int timeLeft, State state, Runnable callback) {
			this.triggetTime = timeLeft + getTime();
			this.state = state;
			this.callback = callback;
		}

		public int timeLeft() {
			return triggetTime - getTime();
		}

		@Override
		public int compareTo(TimeOutEvent o) {
			return timeLeft() - o.timeLeft();
		}

		@Override
		public void run() {
			if (state == Philosopher.get().getState())
				callback.run();
		}
	}

	static {
		timeOuts = new PriorityQueue<>();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					timer++;
					while (!timeOuts.isEmpty() && timeOuts.peek().timeLeft() == 0) {
						TimeOutEvent next = timeOuts.poll();
						next.run();
					}
					try {
						Thread.sleep(TIME_MULTIPLIER);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

}
