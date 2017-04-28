package philosophyOld;

public interface MessageReciever {
	void recieveMessageFrom(Message packet, Side neighbor);
}
