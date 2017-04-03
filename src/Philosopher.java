import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * 
 * @author fredzqm
 */
public class Philosopher {
	public static final int SERVER_PORT = 4848;
	public static boolean verbose = true;
	public static boolean automate = true;
	private static Philosopher philosopher;
	
	
	private final Side left, right;
	private State foodState;
	private BottleManager bottleManager;
	
	private Philosopher(String left, String right) {
		this.left = new Side(left, true);
		this.right = new Side(right, false);
		bottleManager = new BottleManager();
	}

	public void setFoodState(State state) {
		this.foodState = state;
		this.foodState.onStart();
	}

	public State getState() {
		return foodState;
	}

	public Side getRight() {
		return right;
	}

	public Side getLeft() {
		return left;
	}

	public void startServer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ServerSocket s = null;
				Socket client = null;
				try {
					s = new ServerSocket();
					InetSocketAddress myInet = new InetSocketAddress(SERVER_PORT);
					s.bind(myInet);
					while (true) {
						client = s.accept();
						ObjectInputStream in = new ObjectInputStream(client.getInputStream());
						Message packet = (Message) in.readObject();
						String ip = client.getInetAddress().getHostAddress();
						Side neighbor = findNeighbor(ip);
						if (verbose) {
							System.out.println("Recieving request from " + neighbor + " " + packet);
						}
						foodState.recieveMessageFrom(packet, neighbor);
						bottleManager.recieveMessageFrom(packet, neighbor);
						client.close();
					}
				} catch (IOException | ClassNotFoundException e) {
					throw new RuntimeException(e);
				} finally {
					try {
						if (client != null)
							client.close();
						if (s != null)
							s.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			private Side findNeighbor(String ip) {
				if (ip.equals(left.getIP())) {
					return left;
				} else if (ip.equals(right.getIP())) {
					return right;
				} else {
					throw new RuntimeException(
							String.format("Recieved connection from %s\n\tleft:&s\n\tright:%s\n", ip, left, right));
				}
			}
		}).start();
		this.setFoodState(new Thinking());
	}

	public static Philosopher get() {
		return philosopher;
	}

	public static BottleManager getDrinkingManager() {
		return philosopher.bottleManager;
	}

	public static void main(String[] args) {
		philosopher = new Philosopher(args[0], args[1]);
		philosopher.startServer();

		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		while (true) {
			String input = in.nextLine();
			switch (input) {
			case "thinking":
				philosopher.setFoodState(new Thinking());
				break;
			case "hungry":
				philosopher.setFoodState(new Hungry());
				break;
			default:
				System.out.println("Revieved event: " + input);
				break;
			}
		}
	}

}
