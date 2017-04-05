import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Timer {
	private final static int TIME_MULTIPLIER = 3;
	private final static PriorityQueue<TimeOutEvent> waiting;
	private final static BlockingQueue<TimeOutEvent> ready;
	private static int timer;

	public static int getTime() {
		return timer;
	}

	public static void setTimeOut(int min, int max, Runnable callback) {
		setTimeOut((int) (Math.random() * (max - min)) + min, callback);
	}

	public static void setTimeOut(int timeOut, Runnable callback) {
		synchronized (Timer.class) {
			waiting.add(new TimeOutEvent(timeOut, callback));
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
		waiting = new PriorityQueue<>();
		ready = new LinkedBlockingDeque<>();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					while (!waiting.isEmpty() && waiting.peek().timeLeft() == 0) {
						ready.add(waiting.poll());
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
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					TimeOutEvent x;
					try {
						x = ready.take();
						synchronized (Timer.class) {
							x.run();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

}
