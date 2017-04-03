import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

/**
 * 
 * @author fredzqm
 */
public class Philosopher {
	public static final int SERVER_PORT = 4848;
	public static boolean verbose = true;
	public static boolean automate = true;

	private final Neighbor left, right;
	private final boolean leftFirst;
	private State state;
	private Chopstick hasLeftChop, hasRightChop;

	public Philosopher(String left, String right) {
		this.left = new Neighbor(left);
		this.right = new Neighbor(right);
		this.leftFirst = left.compareTo(right) > 0;
		this.hasLeftChop = null;

		this.hasRightChop = new Chopstick();
	}

	public void setState(State state) {
		this.state = state;
		this.state.switchedTo(this);
	}

	public State getState() {
		return state;
	}

	public Collection<Neighbor> getNeighbors() {
		return Arrays.asList(left, right);
	}

	public void setChopstick(Chopstick chopstick, boolean isLeft) {
		if (isLeft)
			this.hasLeftChop = chopstick;
		else
			this.hasRightChop = chopstick;
	}

	public Chopstick getChopstick(boolean isLeft) {
		if (isLeft)
			return this.hasLeftChop;
		else
			return this.hasRightChop;
	}

	public boolean isLeftFirst() {
		return leftFirst;
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
						ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
						ObjectInputStream in = new ObjectInputStream(client.getInputStream());
						Message packet = (Message) in.readObject();
						String ip = client.getInetAddress().getHostAddress();
						Neighbor neighbor = findNeighbor(ip);
						if (verbose) {
							System.out.println("Recieving request from " + neighbor + " " + packet);
						}
						state.recieveMessageFrom(Philosopher.this, packet, neighbor);
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

			private Neighbor findNeighbor(String ip) {
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
		this.setState(new Thinking());
	}

	public static void main(String[] args) {
		Philosopher p = new Philosopher(args[0], args[1]);
		p.startServer();

		Scanner in = new Scanner(System.in);
		while (true) {
			String input = in.nextLine();
			switch (input) {
			case "thinking":
				p.setState(new Thinking());
				break;
			case "hungry":
				p.setState(new Hungry());
				break;
			default:
				System.out.println("Revieved event: " + input);
				break;
			}
		}
	}

}
