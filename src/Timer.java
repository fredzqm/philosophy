import java.util.ArrayList;
import java.util.List;

public class Timer {
	private final static int TIME_MULTIPLIER = 10;
	private final static List<Listener> ls;
	private static long timer;

	public static long getTime() {
		return timer;
	}
	
	public static void registerListener(Listener listener) {
		ls.add(listener);
	}

	static {
		ls = new ArrayList<>();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					timer++;
					try {
						for (Listener l : ls) {
							l.tick(timer);
						}
						Thread.sleep(TIME_MULTIPLIER);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
