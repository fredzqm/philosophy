import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

	private final String left, right;
	private State state;
	private Chopstick hasLeftChop, hasRightChop;

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

	public Philosopher(String left, String right) {
		this.left = left;
		this.right = right;
		this.hasLeftChop = null;
		this.hasRightChop = new Chopstick();
	}

	public void setState(State state) {
		this.state = state;
		this.state.switchedTo(this);
	}

	public String getLeft() {
		return left;
	}

	public String getRight() {
		return right;
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
						Request packet = (Request) in.readObject();
						String name = client.getInetAddress().getHostAddress();
						boolean isLeft = findServer(name);
						System.out.println("Recieving request from " + isLeft + " " + packet);
						Response res = state.recieveRequestFrom(Philosopher.this, packet, isLeft);
						System.out.println("Sending response to " + isLeft + " " + res);
						out.writeObject(res);

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

			private boolean findServer(String name) {
				if (name.equals(left)) {
					return true;
				} else if (name.equals(right)) {
					return false;
				} else {
					throw new RuntimeException(
							String.format("Recieved connection from %s\n\tleft:&s\n\tright:%s\n", name, left, right));
				}
			}
		}).start();
		this.setState(new Thinking());
	}

	public Response talkTo(Request packet, boolean isLeft) {
		String ip = isLeft ? left : right;
		try {
			Socket s = new Socket(ip, SERVER_PORT);
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			
			System.out.println("Sending request to " + isLeft + " " + packet);
			out.writeObject(packet);
			Response response = (Response) in.readObject();
			System.out.println("Recieving response from " + isLeft + " " + response);
			s.close();
			
			return response;
		} catch (ClassNotFoundException | IOException e) {
			throw new RuntimeException(e);
		}
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
