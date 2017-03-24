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
	public static final int SERVER_PORT = 3838;

	private final String left, right;
	private State state;

	public Philosopher(String left, String right) {
		this.state = new Thinking();
		this.left = left;
		this.right = right;
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
				try {
					ServerSocket s = new ServerSocket();
					s.bind(new InetSocketAddress(SERVER_PORT));
					while (true) {
						Socket client = s.accept();
						ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
						ObjectInputStream in = new ObjectInputStream(client.getInputStream());
						Request packet = (Request) in.readObject();

						String name = client.getInetAddress().getCanonicalHostName();
						Response res = state.recieveRequestFrom(Philosopher.this, packet, findServer(name));
						out.writeObject(res);
						
						client.close();
					}
				} catch (IOException | ClassNotFoundException e) {
					throw new RuntimeException(e);
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
	}

	public Response talkTo(Request packet, boolean isLeft) {
		String ip = isLeft ? left : right;
		try {
			Socket s = new Socket(ip, SERVER_PORT);
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			out.writeObject(packet);
			Response response = (Response) in.readObject();
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
