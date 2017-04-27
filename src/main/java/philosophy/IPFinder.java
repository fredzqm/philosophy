package philosophy;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class IPFinder {
	public static String getPublicIPv4() throws UnknownHostException, SocketException {
		Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
		String ipToReturn = null;
		while (e.hasMoreElements()) {
			NetworkInterface n = (NetworkInterface) e.nextElement();
			Enumeration<InetAddress> ee = n.getInetAddresses();
			while (ee.hasMoreElements()) {
				InetAddress i = (InetAddress) ee.nextElement();
				String currentAddress = i.getHostAddress();
				// logger.trace("IP address "+currentAddress+ " found");
				if (!i.isSiteLocalAddress() && !i.isLoopbackAddress() && validate(currentAddress)) {
					ipToReturn = currentAddress;
				}

			}
		}

		return ipToReturn;
	}

	private static final Pattern IPv4RegexPattern = Pattern
			.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

	public static boolean validate(final String ip) {
		return IPv4RegexPattern.matcher(ip).matches();
	}

}
