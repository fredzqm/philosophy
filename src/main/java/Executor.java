
/**
 * A simple example program to use DataMonitor to start and
 * stop executables based on a znode. The program watches the
 * specified znode and saves the data that corresponds to the
 * znode in the filesystem. It also starts the specified program
 * with the specified arguments when the znode exists and kills
 * the program if the znode goes away.
 */
import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class Executor implements Watcher, Runnable, DataMonitor.DataMonitorListener {
	String znode;
	DataMonitor dm;
	ZooKeeper zk;

	public Executor(String hostPort, String znode) throws KeeperException, IOException {
		zk = new ZooKeeper(hostPort, 2181, this);
		dm = new DataMonitor(zk, znode, null, this);

		try {
			zk.setData("/test", "ahfuiewarf".getBytes(), zk.exists("/test", true).getVersion());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String hostPort = "127.0.0.1:2181";
		String znode = "/test";
		try {
			new Executor(hostPort, znode).run();

			System.out.println("End of program");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***************************************************************************
	 * We do process any events ourselves, we just need to forward them on.
	 *
	 * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.proto.WatcherEvent)
	 */
	public void process(WatchedEvent event) {
		dm.process(event);
	}

	public void run() {
		try {
			synchronized (this) {
				wait();
			}
		} catch (InterruptedException e) {
		}
	}

	public void closing(int rc) {
		synchronized (this) {
			notifyAll();
		}
	}

	public void exists(String data) {
		System.out.println(data);
	}
}
