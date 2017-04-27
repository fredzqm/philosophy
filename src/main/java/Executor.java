	/**
	 * A simple example program to use DataMonitor to start and
	 * stop executables based on a znode. The program watches the
	 * specified znode and saves the data that corresponds to the
	 * znode in the filesystem. It also starts the specified program
	 * with the specified arguments when the znode exists and kills
	 * the program if the znode goes away.
	 */
import java.io.IOException;
import java.util.Arrays;

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
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 4) {
			System.err.println("USAGE: Executor hostPort znode filename program [args ...]");
			System.exit(2);
		}
		String hostPort = "2181";
		String znode = "/node";
		try {
			new Executor(hostPort, znode).run();
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
				while (!dm.dead) {
					wait();
				}
			}
		} catch (InterruptedException e) {
		}
	}

	public void closing(int rc) {
		synchronized (this) {
			notifyAll();
		}
	}

	public void exists(byte[] data) {
		System.out.println("Recieved Data: "  + Arrays.toString(data));
		System.out.println("\t\t"  + new String(data));
//		if (data == null) {
//			if (child != null) {
//				System.out.println("Killing process");
//				child.destroy();
//				try {
//					child.waitFor();
//				} catch (InterruptedException e) {
//				}
//			}
//			child = null;
//		} else {
//			if (child != null) {
//				System.out.println("Stopping child");
//				child.destroy();
//				try {
//					child.waitFor();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//			try {
//				FileOutputStream fos = new FileOutputStream(filename);
//				fos.write(data);
//				fos.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			try {
//				System.out.println("Starting child");
//				child = Runtime.getRuntime().exec(exec);
//				new StreamWriter(child.getInputStream(), System.out);
//				new StreamWriter(child.getErrorStream(), System.err);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}
}
