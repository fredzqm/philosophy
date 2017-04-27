package zookeeper;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import zookeeper.DataMonitor.DataMonitorListener;

public class SideMap implements DataMonitorListener, Watcher {
	private ZooKeeper zookeeper;
	// private DataMonitor dm;
	private String znode;

	public SideMap(String connectStr, String znode) {
		try {
			this.zookeeper = new ZooKeeper(connectStr, 2181, this);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.znode = znode;
		
		// create a node at the root
		try {
			this.zookeeper.create(this.znode, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (KeeperException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean containsKey(String key) {
		try {
			return this.zookeeper.exists(getChildZnode(key), false) != null;
		} catch (KeeperException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public String get(String key) {
		try {
			byte[] data = this.zookeeper.getData(getChildZnode(key), false, null);
			return new String(data);
		} catch (InterruptedException | KeeperException e) {
			throw new RuntimeException(e);
		}
	}

	// public void createZnode(String path, String message) throws
	// KeeperException, InterruptedException {
	// zk.create(path, message.getBytes(), Ids.OPEN_ACL_UNSAFE,
	// CreateMode.PERSISTENT);
	// }

	public void put(String key, String data) {
		try {
			if (!this.containsKey(key)) {
				this.zookeeper.create(getChildZnode(key), data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			} else {
				this.zookeeper.setData(getChildZnode(key), "data".getBytes(),
						this.zookeeper.exists(getChildZnode(key), true).getVersion());
			}
		} catch (InterruptedException | KeeperException e) {
			throw new RuntimeException(e);
		}
	}

	private String getChildZnode(String key) {
		return this.znode + "/" + key;
	}

	public String remove(Object arg0) {
		return null;
	}

	@Override
	public void exists(String data) {

	}

	@Override
	public void closing(int rc) {

	}

	@Override
	public void process(WatchedEvent event) {
		// TODO Auto-generated method stub

	}

}
