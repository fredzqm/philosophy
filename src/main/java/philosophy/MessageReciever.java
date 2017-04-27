package philosophy;

public interface MessageReciever {
	void recieveMessageFrom(Message packet, Side neighbor);
}
