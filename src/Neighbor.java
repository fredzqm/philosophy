import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Neighbor {
	private String ip;

	public Neighbor(String ip) {
		this.ip = ip;
	}
	
	public void talkTo(Message packet) {
		try {
			Socket s = new Socket(ip, Philosopher.SERVER_PORT);
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			if (Philosopher.verbose)
				System.out.println("Sending request to " + this + " " + packet);
			out.writeObject(packet);
			s.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return ip;
	}

	public String getIP() {
		return ip;
	}
}
