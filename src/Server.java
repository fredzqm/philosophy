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

	public void addMessageReciever(MessageReciever foodManager) {
		recievers.add(foodManager);
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
						Side neighbor = Philosopher.resoveSide(ip);
						if (verbose) {
							System.out.println("Recieving request from " + neighbor + " " + packet);
						}
						for (MessageReciever mr : recievers)
							mr.recieveMessageFrom(packet, neighbor);
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

		}).start();
	}
}
