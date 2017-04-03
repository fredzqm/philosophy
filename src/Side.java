import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Side {
	private String ip;
	private boolean isLeft;

	public Side(String ip, boolean isLeft) {
		this.ip = ip;
		this.isLeft = isLeft;
	}

	public boolean isLeft() {
		return isLeft;
	}

	public Side getTheOtherSide() {
		if (isLeft)
			return Philosopher.getRight();
		return Philosopher.getLeft();
	}

	public void talkTo(Message packet) {
		try {
			Socket s = new Socket(ip, Philosopher.SERVER_PORT);
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
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
		return isLeft ? "left" : "right";
	}

	public String getIP() {
		return ip;
	}
}
