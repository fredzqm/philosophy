package philosophy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	public static final int SERVER_PORT = 4848;
	public static boolean verbose = true;
	
	private List<MessageReciever> recievers = new ArrayList<>();
	private String ip;

	public Server(String myIP) {
		this.ip = myIP;
	}

	public void addMessageReciever(MessageReciever foodManager) {
		recievers.add(foodManager);
	}

	public void startServer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ServerSocket s = null;
				Side neighbor = null;
				try {
					s = new ServerSocket();
					InetSocketAddress myInet = new InetSocketAddress(SERVER_PORT);
					s.bind(myInet);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				while (true) {
					try {
						Socket client = s.accept();
						ObjectInputStream in = new ObjectInputStream(client.getInputStream());
						Message packet = (Message) in.readObject();
						String ip = client.getInetAddress().getHostAddress();
						neighbor = Philosopher.resolveSide(ip);
						if (verbose) {
							System.out.println("\t\tget " + neighbor + " " + packet);
						}
						for (MessageReciever mr : recievers)
							mr.recieveMessageFrom(packet, neighbor);
						client.close();
					} catch (IOException | ClassNotFoundException e) {
						System.err.println("Error when recieving from " + neighbor + ":" + e.getMessage());
					}
				}
			}

		}).start();
	}
}
