import java.util.PriorityQueue;

public class Timer {
	private final static int TIME_MULTIPLIER = 3;
	private final static PriorityQueue<TimeOutEvent> timeOuts;
	private static int timer;

	public static int getTime() {
		return timer;
	}

	public static void setTimeOut(int min, int max, Runnable callback) {
		setTimeOut((int) (Math.random() * (max - min)) + min, callback);
	}

	public static void setTimeOut(int timeOut, Runnable callback) {
		synchronized (Timer.class) {
			timeOuts.add(new TimeOutEvent(timeOut, callback));
		}
	}

	public static class TimeOutEvent implements Comparable<TimeOutEvent>, Runnable {
		private int triggetTime;
		private Runnable callback;

		public TimeOutEvent(int timeLeft, Runnable callback) {
			this.triggetTime = timeLeft + getTime();
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
			callback.run();
		}
	}

	static {
		timeOuts = new PriorityQueue<>();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					synchronized (Timer.class) {
						while (!timeOuts.isEmpty() && timeOuts.peek().timeLeft() == 0) {
							TimeOutEvent next = timeOuts.poll();
							next.run();
						}
					}
					timer++;
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